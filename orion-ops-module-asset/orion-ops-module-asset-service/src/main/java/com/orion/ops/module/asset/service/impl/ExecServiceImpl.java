package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.function.Functions;
import com.orion.lang.id.UUIds;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.json.matcher.NoMatchStrategy;
import com.orion.lang.utils.json.matcher.ReplacementFormatter;
import com.orion.lang.utils.json.matcher.ReplacementFormatters;
import com.orion.lang.utils.time.Dates;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.constant.FieldConst;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.dao.ExecHostLogDAO;
import com.orion.ops.module.asset.dao.ExecLogDAO;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.define.cache.ExecCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.ExecHostLogDO;
import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.ExecHostLogTailDTO;
import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;
import com.orion.ops.module.asset.entity.dto.ExecParameterSchemaDTO;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.ops.module.asset.entity.vo.ExecCommandHostVO;
import com.orion.ops.module.asset.entity.vo.ExecCommandVO;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.enums.ExecHostStatusEnum;
import com.orion.ops.module.asset.enums.ExecSourceEnum;
import com.orion.ops.module.asset.enums.ExecStatusEnum;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.handler.host.exec.command.ExecTaskExecutors;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandDTO;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandHostDTO;
import com.orion.ops.module.asset.handler.host.exec.command.handler.IExecCommandHandler;
import com.orion.ops.module.asset.handler.host.exec.command.handler.IExecTaskHandler;
import com.orion.ops.module.asset.handler.host.exec.command.manager.ExecTaskManager;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import com.orion.ops.module.asset.service.ExecService;
import com.orion.ops.module.asset.service.HostConfigService;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 批量执行服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:03
 */
@Slf4j
@Service
public class ExecServiceImpl implements ExecService {

    private static final ReplacementFormatter FORMATTER = ReplacementFormatters.create("@{{ ", " }}")
            .noMatchStrategy(NoMatchStrategy.KEEP);

    private static final int DESC_OMIT = 60;

    @Resource
    private FileClient logsFileClient;

    @Resource
    private ExecLogDAO execLogDAO;

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Resource
    private HostConfigService hostConfigService;

    @Resource
    private ExecTaskManager execTaskManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecCommandVO execCommand(ExecCommandRequest request) {
        log.info("ExecService.startExecCommand start params: {}", JSON.toJSONString(request));
        LoginUser user = Objects.requireNonNull(SecurityUtils.getLoginUser());
        Long userId = user.getId();
        String command = request.getCommand();
        List<Long> hostIdList = request.getHostIdList();
        // 检查主机权限
        List<Long> authorizedHostIdList = assetAuthorizedDataService.getUserAuthorizedHostId(userId, HostConfigTypeEnum.SSH);
        hostIdList.removeIf(s -> !authorizedHostIdList.contains(s));
        log.info("ExecService.startExecCommand host hostList: {}", hostIdList);
        Valid.notEmpty(hostIdList, ErrorMessage.CHECK_AUTHORIZED_HOST);
        List<HostDO> hosts = hostDAO.selectBatchIds(hostIdList);
        // 插入日志
        ExecLogDO execLog = ExecLogDO.builder()
                .userId(userId)
                .username(user.getUsername())
                .source(ExecSourceEnum.BATCH.name())
                .description(Strings.ifBlank(request.getDescription(), () -> {
                    if (command.length() < DESC_OMIT + 3) {
                        return command;
                    } else {
                        return command.substring(0, DESC_OMIT) + Const.OMIT;
                    }
                }))
                .command(command)
                .parameterSchema(request.getParameterSchema())
                .timeout(request.getTimeout())
                .status(ExecStatusEnum.WAITING.name())
                .build();
        execLogDAO.insert(execLog);
        Long execId = execLog.getId();
        // 获取内置参数
        Map<String, Object> builtinsParams = this.getBaseBuiltinsParams(user, execId, request.getParameterSchema());
        // 设置主机日志
        List<ExecHostLogDO> execHostLogs = hosts.stream()
                .map(s -> {
                    String parameter = JSON.toJSONString(this.getHostParams(builtinsParams, s));
                    return ExecHostLogDO.builder()
                            .logId(execId)
                            .hostId(s.getId())
                            .hostName(s.getName())
                            .hostAddress(s.getAddress())
                            .status(ExecHostStatusEnum.WAITING.name())
                            .command(FORMATTER.format(command, parameter))
                            .parameter(parameter)
                            .logPath(this.buildLogPath(execId, s.getId()))
                            .build();
                }).collect(Collectors.toList());
        execHostLogDAO.insertBatch(execHostLogs);
        // 操作日志
        OperatorLogs.add(OperatorLogs.ID, execId);
        // 开始执行
        this.startExec(execLog, execHostLogs);
        // 返回
        List<ExecCommandHostVO> hostResult = execHostLogs.stream()
                .map(s -> ExecCommandHostVO.builder()
                        .id(s.getId())
                        .hostId(s.getHostId())
                        .hostName(s.getHostName())
                        .hostAddress(s.getHostAddress())
                        .status(s.getStatus())
                        .build())
                .collect(Collectors.toList());
        return ExecCommandVO.builder()
                .id(execId)
                .status(execLog.getStatus())
                .hosts(hostResult)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecCommandVO reExecCommand(Long logId) {
        log.info("ExecService.reExecCommand start logId: {}", logId);
        // 获取执行记录
        ExecLogDO execLog = execLogDAO.selectById(logId);
        Valid.notNull(execLog, ErrorMessage.DATA_ABSENT);
        // 获取执行主机
        List<ExecHostLogDO> hostLogs = execHostLogDAO.selectByLogId(logId);
        Valid.notEmpty(hostLogs, ErrorMessage.DATA_ABSENT);
        List<Long> hostIdList = hostLogs.stream()
                .map(ExecHostLogDO::getHostId)
                .collect(Collectors.toList());
        // 调用执行方法
        ExecCommandRequest request = ExecCommandRequest.builder()
                .description(execLog.getDescription())
                .timeout(execLog.getTimeout())
                .command(execLog.getCommand())
                .parameterSchema(execLog.getParameterSchema())
                .hostIdList(hostIdList)
                .build();
        return this.execCommand(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interruptExec(Long logId) {
        log.info("ExecService.interruptExec start logId: {}", logId);
        // 获取执行记录
        ExecLogDO execLog = execLogDAO.selectById(logId);
        Valid.notNull(execLog, ErrorMessage.DATA_ABSENT);
        // 检查状态
        if (!ExecStatusEnum.of(execLog.getStatus()).isCloseable()) {
            return;
        }
        // 中断执行
        IExecTaskHandler task = execTaskManager.getTask(logId);
        if (task != null) {
            log.info("ExecService.interruptExec interrupted logId: {}", logId);
            // 中断
            task.interrupted();
        } else {
            log.info("ExecService.interruptExec updateStatus start logId: {}", logId);
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
            log.info("ExecService.interruptExec updateStatus finish logId: {}, effect: {}", logId, effect);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void interruptHostExec(Long hostLogId) {
        log.info("ExecService.interruptHostExec start hostLogId: {}", hostLogId);
        // 获取执行记录
        ExecHostLogDO hostLog = execHostLogDAO.selectById(hostLogId);
        Valid.notNull(hostLog, ErrorMessage.DATA_ABSENT);
        Long logId = hostLog.getLogId();
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
            log.info("ExecService.interruptHostExec interrupted logId: {}, hostLogId: {}", logId, hostLogId);
            IExecCommandHandler handler = task.getHandlers()
                    .stream()
                    .filter(s -> s.getHostId().equals(hostLog.getHostId()))
                    .findFirst()
                    .orElse(null);
            // 中断
            if (handler != null) {
                handler.interrupted();
            }
        } else {
            log.info("ExecService.interruptHostExec updateStatus start logId: {}, hostLogId: {}", logId, hostLogId);
            // 不存在则直接修改状态
            ExecHostLogDO updateHost = new ExecHostLogDO();
            updateHost.setId(hostLogId);
            updateHost.setStatus(ExecHostStatusEnum.INTERRUPTED.name());
            updateHost.setFinishTime(new Date());
            int effect = execHostLogDAO.updateById(updateHost);
            // 查询执行状态
            ExecLogDO execLog = execLogDAO.selectById(logId);
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
            log.info("ExecService.interruptHostExec updateStatus finish logId: {}, hostLogId: {}, effect: {}", logId, hostLogId, effect);
        }
    }

    @Override
    public String getExecLogTailToken(ExecLogTailRequest request) {
        Long execId = request.getExecId();
        List<Long> hostExecIdList = request.getHostExecIdList();
        log.info("ExecService.getExecLogTailToken start execId: {}, hostExecIdList: {}", execId, hostExecIdList);
        // 查询执行日志
        ExecLogDO execLog = execLogDAO.selectById(execId);
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
        Map<Long, HostConfigVO> configMap = hostConfigService.getHostConfigList(hostIdList, HostConfigTypeEnum.SSH.getType())
                .stream()
                .collect(Collectors.toMap(HostConfigVO::getId, Function.identity()));
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
                                        .map(HostConfigVO::getConfig)
                                        .map(c -> c.get(FieldConst.CHARSET))
                                        .map(Objects1::toString)
                                        .orElse(Const.UTF_8))
                                .build())
                        .collect(Collectors.toList()))
                .build();
        // 设置缓存
        RedisStrings.setJson(cacheKey, ExecCacheKeyDefine.EXEC_TAIL, cache);
        log.info("ExecService.getExecLogTailToken finish token: {}, execId: {}, hostExecIdList: {}", token, execId, hostExecIdList);
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
    public void downloadLogFile(Long id, HttpServletResponse response) {
        log.info("ExecService.downloadLogFile id: {}", id);
        try {
            // 获取主机执行日志
            ExecHostLogDO hostLog = execHostLogDAO.selectById(id);
            Valid.notNull(hostLog, ErrorMessage.LOG_ABSENT);
            String logPath = hostLog.getLogPath();
            Valid.notNull(logPath, ErrorMessage.LOG_ABSENT);
            // 设置日志参数
            OperatorLogs.add(OperatorLogs.LOG_ID, hostLog.getLogId());
            OperatorLogs.add(OperatorLogs.HOST_ID, hostLog.getHostId());
            OperatorLogs.add(OperatorLogs.HOST_NAME, hostLog.getHostName());
            // 获取日志
            InputStream in = logsFileClient.getContentInputStream(logPath);
            // 返回
            Servlets.transfer(response, in, Files1.getFileName(logPath));
        } catch (Exception e) {
            log.error("ExecService.downloadLogFile error id: {}", id, e);
            String errorMessage = ErrorMessage.FILE_READ_ERROR;
            if (e instanceof InvalidArgumentException) {
                errorMessage = e.getMessage();
            }
            // 响应错误信息
            try {
                Servlets.transfer(response, Strings.bytes(errorMessage), Const.ERROR_LOG);
            } catch (Exception ex) {
                log.error("ExecService.downloadLogFile transfer-error id: {}", id, ex);
            }
        }
    }

    /**
     * 开始执行命令
     *
     * @param execLog      execLog
     * @param execHostLogs hostLogs
     */
    private void startExec(ExecLogDO execLog, List<ExecHostLogDO> execHostLogs) {
        ExecCommandDTO exec = ExecCommandDTO.builder()
                .logId(execLog.getId())
                .timeout(execLog.getTimeout())
                .hosts(execHostLogs.stream()
                        .map(s -> ExecCommandHostDTO.builder()
                                .hostId(s.getHostId())
                                .hostLogId(s.getId())
                                .command(s.getCommand())
                                .timeout(execLog.getTimeout())
                                .logPath(s.getLogPath())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        ExecTaskExecutors.start(exec);
    }

    /**
     * 构建日志路径
     *
     * @param logId  logId
     * @param hostId hostId
     * @return logPath
     */
    private String buildLogPath(Long logId, Long hostId) {
        String logFile = "/exec/" + logId + "/" + logId + "_" + hostId + ".log";
        return logsFileClient.getReturnPath(logFile);
    }

    /**
     * 提取参数
     *
     * @param parameterSchema parameterSchema
     * @return params
     */
    private Map<String, Object> extraSchemaParams(String parameterSchema) {
        List<ExecParameterSchemaDTO> schemaList = JSON.parseArray(parameterSchema, ExecParameterSchemaDTO.class);
        if (Lists.isEmpty(schemaList)) {
            return Maps.newMap();
        }
        // 解析参数
        return schemaList.stream()
                .collect(Collectors.toMap(ExecParameterSchemaDTO::getName,
                        ExecParameterSchemaDTO::getValue,
                        Functions.right()));
    }

    /**
     * 获取基础内置参数
     *
     * @param user            user
     * @param execId          execId
     * @param parameterSchema parameterSchema
     * @return params
     */
    private Map<String, Object> getBaseBuiltinsParams(LoginUser user, Long execId, String parameterSchema) {
        String uuid = UUIds.random();
        Date date = new Date();
        // 输入参数
        Map<String, Object> params = this.extraSchemaParams(parameterSchema);
        // 添加内置参数
        params.put("userId", user.getId());
        params.put("username", user.getId());
        params.put("execId", execId);
        params.put("uuid", uuid);
        params.put("uuidShort", uuid.replace("-", Strings.EMPTY));
        params.put("timestampMillis", date.getTime());
        params.put("timestamp", date.getTime() / Dates.SECOND_STAMP);
        params.put("date", Dates.format(date, Dates.YMD));
        params.put("datetime", Dates.format(date, Dates.YMD_HMS));
        return params;
    }

    /**
     * 获取主机参数
     *
     * @param baseParams baseParams
     * @param host       host
     * @return params
     */
    private Map<String, Object> getHostParams(Map<String, Object> baseParams, HostDO host) {
        String uuid = UUIds.random();
        Map<String, Object> params = Maps.newMap(baseParams);
        params.put("hostId", host.getId());
        params.put("hostName", host.getName());
        params.put("hostCode", host.getCode());
        params.put("hostAddress", host.getAddress());
        params.put("hostUuid", uuid);
        params.put("hostUuidShort", uuid.replace("-", Strings.EMPTY));
        return params;
    }

}
