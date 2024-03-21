package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.ExecHostLogConvert;
import com.orion.ops.module.asset.convert.ExecLogConvert;
import com.orion.ops.module.asset.dao.ExecHostLogDAO;
import com.orion.ops.module.asset.dao.ExecLogDAO;
import com.orion.ops.module.asset.entity.domain.ExecHostLogDO;
import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.ExecHostLogVO;
import com.orion.ops.module.asset.entity.vo.ExecLogStatusVO;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;
import com.orion.ops.module.asset.handler.host.exec.command.handler.IExecTaskHandler;
import com.orion.ops.module.asset.handler.host.exec.command.manager.ExecTaskManager;
import com.orion.ops.module.asset.service.ExecHostLogService;
import com.orion.ops.module.asset.service.ExecLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Override
    public DataGrid<ExecLogVO> getExecLogPage(ExecLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execLogDAO.of(wrapper)
                .page(request)
                .dataGrid(ExecLogConvert.MAPPER::to);
    }

    @Override
    public ExecLogVO getExecLog(Long id, String source) {
        // 查询执行日志
        ExecLogDO row = execLogDAO.of()
                .createValidateWrapper()
                .eq(ExecLogDO::getId, id)
                .eq(ExecLogDO::getSource, source)
                .then()
                .getOne();
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
    public ExecLogStatusVO getExecLogStatus(List<Long> idList) {
        // 查询执行状态
        List<ExecLogVO> logList = execLogDAO.of()
                .createWrapper()
                .select(ExecLogDO::getId,
                        ExecLogDO::getStatus,
                        ExecLogDO::getStartTime,
                        ExecLogDO::getFinishTime)
                .in(ExecLogDO::getId, idList)
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
                        ExecHostLogDO::getExitStatus,
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
    public Long queryExecLogCount(ExecLogQueryRequest request) {
        return execLogDAO.selectCount(this.buildQueryWrapper(request));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecLogById(Long id) {
        log.info("ExecLogService-deleteExecLogById id: {}", id);
        // 检查数据是否存在
        ExecLogDO record = execLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 中断命令执行
        this.interruptedTask(Lists.singleton(id));
        // 删除执行日志
        int effect = execLogDAO.deleteById(id);
        // 删除主机日志
        execHostLogService.deleteExecHostLogByLogId(Lists.singleton(id));
        log.info("ExecLogService-deleteExecLogById id: {}, effect: {}", id, effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExecLogByIdList(List<Long> idList) {
        log.info("ExecLogService-deleteExecLogByIdList idList: {}", idList);
        // 中断命令执行
        this.interruptedTask(idList);
        // 删除执行日志
        int effect = execLogDAO.deleteBatchIds(idList);
        // 删除主机日志
        execHostLogService.deleteExecHostLogByLogId(idList);
        log.info("ExecLogService-deleteExecLogByIdList effect: {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearExecLog(ExecLogQueryRequest request) {
        log.info("ExecLogService.clearExecLog start {}", JSON.toJSONString(request));
        // 查询
        LambdaQueryWrapper<ExecLogDO> wrapper = this.buildQueryWrapper(request)
                .select(ExecLogDO::getId);
        List<Long> idList = execLogDAO.selectList(wrapper)
                .stream()
                .map(ExecLogDO::getId)
                .collect(Collectors.toList());
        int effect = 0;
        if (!idList.isEmpty()) {
            // 中断命令执行
            this.interruptedTask(idList);
            // 删除执行日志
            effect = execLogDAO.delete(wrapper);
            // 删除主机日志
            execHostLogService.deleteExecHostLogByLogId(idList);
        }
        log.info("ExecLogService.clearExecLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<ExecLogDO> buildQueryWrapper(ExecLogQueryRequest request) {
        return execLogDAO.wrapper()
                .eq(ExecLogDO::getId, request.getId())
                .eq(ExecLogDO::getUserId, request.getUserId())
                .eq(ExecLogDO::getUsername, request.getUsername())
                .eq(ExecLogDO::getSource, request.getSource())
                .eq(ExecLogDO::getSourceId, request.getSourceId())
                .like(ExecLogDO::getDescription, request.getDescription())
                .like(ExecLogDO::getCommand, request.getCommand())
                .eq(ExecLogDO::getStatus, request.getStatus())
                .ge(ExecLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(ExecLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .orderByDesc(ExecLogDO::getId);
    }

    /**
     * 中断任务
     *
     * @param idList idList
     */
    private void interruptedTask(List<Long> idList) {
        idList.stream()
                .map(execTaskManager::getTask)
                .filter(Objects::nonNull)
                .forEach(IExecTaskHandler::interrupted);
    }

}
