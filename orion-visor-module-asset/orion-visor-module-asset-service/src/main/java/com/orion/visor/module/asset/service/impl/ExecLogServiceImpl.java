package com.orion.visor.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.annotation.Keep;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.id.UUIds;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.constant.FileConst;
import com.orion.visor.framework.common.enums.EndpointDefine;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.framework.common.utils.SqlUtils;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.convert.ExecHostLogConvert;
import com.orion.visor.module.asset.convert.ExecLogConvert;
import com.orion.visor.module.asset.dao.ExecHostLogDAO;
import com.orion.visor.module.asset.dao.ExecLogDAO;
import com.orion.visor.module.asset.define.cache.ExecCacheKeyDefine;
import com.orion.visor.module.asset.entity.domain.ExecHostLogDO;
import com.orion.visor.module.asset.entity.domain.ExecLogDO;
import com.orion.visor.module.asset.entity.dto.ExecHostLogTailDTO;
import com.orion.visor.module.asset.entity.dto.ExecLogTailDTO;
import com.orion.visor.module.asset.entity.request.exec.ExecLogClearRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.visor.module.asset.entity.vo.ExecHostLogVO;
import com.orion.visor.module.asset.entity.vo.ExecLogStatusVO;
import com.orion.visor.module.asset.entity.vo.ExecLogVO;
import com.orion.visor.module.asset.enums.ExecHostStatusEnum;
import com.orion.visor.module.asset.enums.ExecStatusEnum;
import com.orion.visor.module.asset.enums.HostTypeEnum;
import com.orion.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.visor.module.asset.handler.host.exec.command.handler.IExecCommandHandler;
import com.orion.visor.module.asset.handler.host.exec.command.handler.IExecTaskHandler;
import com.orion.visor.module.asset.handler.host.exec.command.manager.ExecTaskManager;
import com.orion.visor.module.asset.service.ExecHostLogService;
import com.orion.visor.module.asset.service.ExecLogService;
import com.orion.visor.module.asset.service.HostConfigService;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 批量执行日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Slf4j
@Service
public class ExecLogServiceImpl implements ExecLogService {

    @Resource
    private ExecLogDAO execLogDAO;

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Resource
    private ExecHostLogService execHostLogService;

    @Resource
    private ExecTaskManager execTaskManager;

    @Resource
    private HostConfigService hostConfigService;

    @Keep
    @Resource
    private FileClient logsFileClient;

    @Override
    public DataGrid<ExecLogVO> getExecLogPage(ExecLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecLogDO> wrapper = this.buildQueryWrapper(request)
                .orderByDesc(ExecLogDO::getId);
        // 查询
        return execLogDAO.of(wrapper)
                .page(request)
                .dataGrid(ExecLogConvert.MAPPER::to);
    }

    @Override
    public ExecLogVO getExecLog(Long id, String source) {
        // 查询执行日志
        ExecLogDO row = execLogDAO.selectByIdSource(id, source);
        Valid.notNull(row, ErrorMessage.LOG_ABSENT);
        // 查询执行主机
        List<ExecHostLogDO> hosts = execHostLogDAO.selectByLogId(id);
        // 返回
        ExecLogVO vo = ExecLogConvert.MAPPER.to(row);
        vo.setHosts(ExecHostLogConvert.MAPPER.to(hosts));
        return vo;
    }

    @Override
    public List<ExecLogVO> getExecHistory(ExecLogQueryRequest request) {
        // 查询执行记录
        List<ExecLogDO> rows = execLogDAO.getExecHistory(request.getSource(), request.getUserId(), request.getLimit());
        if (rows.isEmpty()) {
            return Lists.empty();
        }
        List<ExecLogVO> logs = ExecLogConvert.MAPPER.to(rows);
        List<Long> logIdList = logs.stream()
                .map(ExecLogVO::getId)
                .collect(Collectors.toList());
        // 设置执行主机id
        Map<Long, List<Long>> hostIdRel = execHostLogDAO.of()
                .createWrapper()
                .select(ExecHostLogDO::getId, ExecHostLogDO::getLogId, ExecHostLogDO::getHostId)
                .in(ExecHostLogDO::getLogId, logIdList)
                .then()
                .stream()
                .collect(Collectors.groupingBy(
                        ExecHostLogDO::getLogId,
                        Collectors.mapping(
                                ExecHostLogDO::getHostId,
                                Collectors.toList()
                        )
                ));
        logs.forEach(s -> s.setHostIdList(hostIdRel.get(s.getId())));
        return logs;
    }

    @Override
    public ExecLogStatusVO getExecLogStatus(List<Long> idList, String source) {
        // 查询执行状态
        List<ExecLogVO> logList = execLogDAO.of()
                .createWrapper()
                .select(ExecLogDO::getId,
                        ExecLogDO::getStatus,
                        ExecLogDO::getStartTime,
                        ExecLogDO::getFinishTime)
                .in(ExecLogDO::getId, idList)
                .eq(ExecLogDO::getSource, source)
                .then()
                .list(ExecLogConvert.MAPPER::to);
        // 查询主机状态
        List<ExecHostLogVO> hostList = execHostLogDAO.of()
                .createWrapper()
                .select(ExecHostLogDO::getId,
                        ExecHostLogDO::getLogId,
                        ExecHostLogDO::getStatus,
                        ExecHostLogDO::getStartTime,
                        ExecHostLogDO::getFinishTime,
                        ExecHostLogDO::getExitCode,
                        ExecHostLogDO::getErrorMessage)
                .in(ExecHostLogDO::getLogId, idList)
                .then()
                .list(ExecHostLogConvert.MAPPER::to);
        // 返回
        return ExecLogStatusVO.builder()
                .logList(logList)
                .hostList(hostList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecLogById(Long id, String source) {
        return this.deleteExecLogByIdList(Lists.singleton(id), source);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecLogByIdList(List<Long> idList, String source) {
        log.info("ExecLogService-deleteExecLogByIdList idList: {}", idList);
        int count = execLogDAO.of()
                .createWrapper()
                .in(ExecLogDO::getId, idList)
                .eq(ExecLogDO::getSource, source)
                .then()
                .count()
                .intValue();
        Valid.isTrue(idList.size() == count, ErrorMessage.DATA_MODIFIED);
        // 删除
        return this.deleteExecLogByIdList(idList);
    }

    @Override
    public Integer deleteExecLogByIdList(List<Long> idList) {
        log.info("ExecLogService-deleteExecLogByIdList start: {}", idList);
        if (Lists.isEmpty(idList)) {
            OperatorLogs.add(OperatorLogs.COUNT, Const.N_0);
            return Const.N_0;
        }
        // 中断命令执行
        this.interruptTask(idList);
        // 删除执行日志
        int effect = execLogDAO.deleteBatchIds(idList);
        // 删除主机日志
        execHostLogService.deleteExecHostLogByLogId(idList);
        log.info("ExecLogService-deleteExecLogByIdList end effect: {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        // 异步删除文件
        SpringHolder.getBean(ExecLogService.class).asyncDeleteLogFiles(idList);
        return effect;
    }

    @Override
    public Long queryExecLogCount(ExecLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execLogDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearExecLog(ExecLogClearRequest request) {
        log.info("ExecLogService.clearExecLog start {}", JSON.toJSONString(request));
        // 查询
        LambdaQueryWrapper<ExecLogDO> wrapper = this.buildQueryWrapper(request)
                .select(ExecLogDO::getId)
                .orderByAsc(ExecLogDO::getId)
                .last(SqlUtils.limit(request.getLimit()));
        List<Long> idList = execLogDAO.selectList(wrapper)
                .stream()
                .map(ExecLogDO::getId)
                .collect(Collectors.toList());
        // 删除
        return this.deleteExecLogByIdList(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interruptExec(Long logId, String source) {
        log.info("ExecLogService.interruptExec start logId: {}, source: {}", logId, source);
        // 获取执行记录
        ExecLogDO execLog = execLogDAO.selectByIdSource(logId, source);
        Valid.notNull(execLog, ErrorMessage.DATA_ABSENT);
        // 检查状态
        if (!ExecStatusEnum.of(execLog.getStatus()).isCloseable()) {
            return;
        }
        // 中断执行
        IExecTaskHandler task = execTaskManager.getTask(logId);
        if (task != null) {
            log.info("ExecLogService.interruptExec interrupt logId: {}", logId);
            // 中断
            task.interrupt();
        } else {
            log.info("ExecLogService.interruptExec updateStatus start logId: {}", logId);
            // 不存在则直接修改状态
            ExecLogDO updateExec = new ExecLogDO();
            updateExec.setId(logId);
            updateExec.setStatus(ExecStatusEnum.COMPLETED.name());
            updateExec.setFinishTime(new Date());
            int effect = execLogDAO.updateById(updateExec);
            // 更新主机状态
            ExecHostLogDO updateHost = new ExecHostLogDO();
            updateHost.setStatus(ExecHostStatusEnum.INTERRUPTED.name());
            updateHost.setFinishTime(new Date());
            LambdaQueryWrapper<ExecHostLogDO> updateHostWrapper = execHostLogDAO.lambda()
                    .eq(ExecHostLogDO::getLogId, logId)
                    .in(ExecHostLogDO::getStatus, ExecHostStatusEnum.CLOSEABLE_STATUS);
            effect += execHostLogDAO.update(updateHost, updateHostWrapper);
            log.info("ExecLogService.interruptExec updateStatus finish logId: {}, effect: {}", logId, effect);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interruptHostExec(Long hostLogId, String source) {
        log.info("ExecLogService.interruptHostExec start hostLogId: {}, source: {}", hostLogId, source);
        // 获取执行记录
        ExecHostLogDO hostLog = execHostLogDAO.selectById(hostLogId);
        Valid.notNull(hostLog, ErrorMessage.DATA_ABSENT);
        Long logId = hostLog.getLogId();
        ExecLogDO execLog = execLogDAO.selectByIdSource(logId, source);
        Valid.notNull(execLog, ErrorMessage.DATA_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.LOG_ID, logId);
        OperatorLogs.add(OperatorLogs.HOST_NAME, hostLog.getHostName());
        // 检查状态
        if (!ExecHostStatusEnum.of(hostLog.getStatus()).isCloseable()) {
            return;
        }
        // 中断执行
        IExecTaskHandler task = execTaskManager.getTask(logId);
        if (task != null) {
            log.info("ExecLogService.interruptHostExec interrupt logId: {}, hostLogId: {}", logId, hostLogId);
            IExecCommandHandler handler = task.getHandlers()
                    .stream()
                    .filter(s -> s.getHostId().equals(hostLog.getHostId()))
                    .findFirst()
                    .orElse(null);
            // 中断
            if (handler != null) {
                handler.interrupt();
            }
        } else {
            log.info("ExecLogService.interruptHostExec updateStatus start logId: {}, hostLogId: {}", logId, hostLogId);
            // 不存在则直接修改状态
            ExecHostLogDO updateHost = new ExecHostLogDO();
            updateHost.setId(hostLogId);
            updateHost.setStatus(ExecHostStatusEnum.INTERRUPTED.name());
            updateHost.setFinishTime(new Date());
            int effect = execHostLogDAO.updateById(updateHost);
            // 检查是否可关闭
            if (ExecStatusEnum.of(execLog.getStatus()).isCloseable()) {
                // 状态可修改则需要检查其他主机任务是否已经完成
                Long closeableCount = execHostLogDAO.of()
                        .createWrapper()
                        .eq(ExecHostLogDO::getLogId, logId)
                        .in(ExecHostLogDO::getStatus, ExecHostStatusEnum.CLOSEABLE_STATUS)
                        .then()
                        .count();
                if (closeableCount == 0) {
                    // 修改任务状态
                    ExecLogDO updateExec = new ExecLogDO();
                    updateExec.setId(logId);
                    updateExec.setStatus(ExecStatusEnum.COMPLETED.name());
                    updateExec.setFinishTime(new Date());
                    effect += execLogDAO.updateById(updateExec);
                }
            }
            log.info("ExecLogService.interruptHostExec updateStatus finish logId: {}, hostLogId: {}, effect: {}", logId, hostLogId, effect);
        }
    }

    @Override
    public String getExecLogTailToken(ExecLogTailRequest request) {
        String source = request.getSource();
        Long execId = request.getExecId();
        List<Long> hostExecIdList = request.getHostExecIdList();
        log.info("ExecLogService.getExecLogTailToken start execId: {}, hostExecIdList: {}", execId, hostExecIdList);
        // 查询执行日志
        ExecLogDO execLog = execLogDAO.selectByIdSource(execId, source);
        Valid.notNull(execLog, ErrorMessage.LOG_ABSENT);
        // 查询主机日志
        List<ExecHostLogDO> hostLogs;
        if (hostExecIdList == null) {
            hostLogs = execHostLogDAO.selectByLogId(execId);
        } else {
            hostLogs = execHostLogDAO.of()
                    .createWrapper()
                    .eq(ExecHostLogDO::getLogId, execId)
                    .in(ExecHostLogDO::getId, hostExecIdList)
                    .then()
                    .list();
        }
        Valid.notEmpty(hostLogs, ErrorMessage.LOG_ABSENT);
        // 获取编码集
        List<Long> hostIdList = hostLogs.stream()
                .map(ExecHostLogDO::getHostId)
                .collect(Collectors.toList());
        Map<Long, HostSshConfigModel> configMap = hostConfigService.getHostConfigMap(hostIdList, HostTypeEnum.SSH);
        // 生成缓存
        String token = UUIds.random19();
        String cacheKey = ExecCacheKeyDefine.EXEC_TAIL.format(token);
        ExecLogTailDTO cache = ExecLogTailDTO.builder()
                .token(token)
                .id(execId)
                .userId(SecurityUtils.getLoginUserId())
                .hosts(hostLogs.stream()
                        .map(s -> ExecHostLogTailDTO.builder()
                                .id(s.getId())
                                .hostId(s.getHostId())
                                .path(s.getLogPath())
                                .charset(Optional.ofNullable(configMap.get(s.getHostId()))
                                        .map(HostSshConfigModel::getCharset)
                                        .map(Objects1::toString)
                                        .orElse(Const.UTF_8))
                                .build())
                        .collect(Collectors.toList()))
                .build();
        // 设置缓存
        RedisStrings.setJson(cacheKey, ExecCacheKeyDefine.EXEC_TAIL, cache);
        log.info("ExecLogService.getExecLogTailToken finish token: {}, execId: {}, hostExecIdList: {}", token, execId, hostExecIdList);
        return token;
    }

    @Override
    public ExecLogTailDTO getExecLogTailInfo(String token) {
        String cacheKey = ExecCacheKeyDefine.EXEC_TAIL.format(token);
        // 获取缓存
        ExecLogTailDTO tail = RedisStrings.getJson(cacheKey, ExecCacheKeyDefine.EXEC_TAIL);
        if (tail != null) {
            // 删除缓存
            RedisStrings.delete(cacheKey);
        }
        return tail;
    }

    @Override
    public void downloadLogFile(Long id, String source, HttpServletResponse response) {
        log.info("ExecLogService.downloadLogFile id: {}, source: {}", id, source);
        InputStream in = null;
        try {
            // 获取主机执行日志
            ExecHostLogDO hostLog = execHostLogDAO.selectById(id);
            Valid.notNull(hostLog, ErrorMessage.LOG_ABSENT);
            String logPath = hostLog.getLogPath();
            Valid.notNull(logPath, ErrorMessage.LOG_ABSENT);
            ExecLogDO execLog = execLogDAO.selectByIdSource(hostLog.getLogId(), source);
            Valid.notNull(execLog, ErrorMessage.LOG_ABSENT);
            // 设置日志参数
            OperatorLogs.add(OperatorLogs.LOG_ID, hostLog.getLogId());
            OperatorLogs.add(OperatorLogs.HOST_ID, hostLog.getHostId());
            OperatorLogs.add(OperatorLogs.HOST_NAME, hostLog.getHostName());
            // 获取日志
            in = logsFileClient.getContentInputStream(logPath);
            // 返回
            Servlets.transfer(response, in, Files1.getFileName(logPath));
        } catch (Exception e) {
            log.error("ExecLogService.downloadLogFile error id: {}", id, e);
            Streams.close(in);
            // 获取错误信息
            String errorMessage = ErrorMessage.getErrorMessage(e, ErrorMessage.FILE_READ_ERROR_CLEAR);
            // 响应错误信息
            try {
                Servlets.transfer(response, Strings.bytes(errorMessage), FileConst.ERROR_LOG);
            } catch (Exception ex) {
                log.error("ExecLogService.downloadLogFile transfer-error id: {}", id, ex);
            }
        }
    }

    @Override
    @Async("asyncExecutor")
    public void asyncDeleteLogFiles(List<Long> idList) {
        if (Lists.isEmpty(idList)) {
            return;
        }
        // 删除
        idList.stream()
                .map(s -> EndpointDefine.EXEC_LOG.format(s, Const.EMPTY))
                .map(logsFileClient::getReturnPath)
                .map(logsFileClient::getAbsolutePath)
                .map(Files1::getParentPath)
                .map(File::new)
                .forEach(Files1::delete);
    }

    @Override
    public LambdaQueryWrapper<ExecLogDO> buildQueryWrapper(ExecLogQueryRequest request) {
        return execLogDAO.wrapper()
                .eq(ExecLogDO::getId, request.getId())
                .eq(ExecLogDO::getUserId, request.getUserId())
                .eq(ExecLogDO::getUsername, request.getUsername())
                .eq(ExecLogDO::getSource, request.getSource())
                .eq(ExecLogDO::getSourceId, request.getSourceId())
                .like(ExecLogDO::getDescription, request.getDescription())
                .like(ExecLogDO::getCommand, request.getCommand())
                .eq(ExecLogDO::getStatus, request.getStatus())
                .in(ExecLogDO::getStatus, request.getStatusList())
                .ge(ExecLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(ExecLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .le(ExecLogDO::getCreateTime, request.getCreateTimeLe());
    }

    /**
     * 中断任务
     *
     * @param idList idList
     */
    private void interruptTask(List<Long> idList) {
        idList.stream()
                .map(execTaskManager::getTask)
                .filter(Objects::nonNull)
                .forEach(IExecTaskHandler::interrupt);
    }

}
