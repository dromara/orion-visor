package com.orion.visor.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.collect.Lists;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.SqlUtils;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.convert.HostConnectLogConvert;
import com.orion.visor.module.asset.dao.HostConnectLogDAO;
import com.orion.visor.module.asset.entity.domain.HostConnectLogDO;
import com.orion.visor.module.asset.entity.dto.HostConnectLogExtraDTO;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogClearRequest;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.visor.module.asset.entity.vo.HostConnectLogVO;
import com.orion.visor.module.asset.enums.HostConnectStatusEnum;
import com.orion.visor.module.asset.enums.HostConnectTypeEnum;
import com.orion.visor.module.asset.handler.host.terminal.manager.HostTerminalManager;
import com.orion.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.visor.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.visor.module.asset.service.HostConnectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 主机连接日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Slf4j
@Service
public class HostConnectLogServiceImpl implements HostConnectLogService {

    @Resource
    private HostConnectLogDAO hostConnectLogDAO;

    @Resource
    private HostTerminalManager hostTerminalManager;

    @Override
    public HostConnectLogDO create(HostConnectTypeEnum type, HostConnectLogCreateRequest request) {
        HostConnectLogDO record = HostConnectLogConvert.MAPPER.to(request);
        record.setType(type.name());
        String status = request.getStatus();
        record.setStatus(status);
        record.setStartTime(new Date());
        record.setExtraInfo(JSON.toJSONString(request.getExtra()));
        // 失败直接设置结束时间
        if (HostConnectStatusEnum.FAILED.name().equals(status)) {
            record.setEndTime(new Date());
        }
        hostConnectLogDAO.insert(record);
        return record;
    }

    @Override
    public DataGrid<HostConnectLogVO> getHostConnectLogPage(HostConnectLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request)
                .orderByDesc(HostConnectLogDO::getId);
        // 查询
        return hostConnectLogDAO.of(wrapper)
                .page(request)
                .dataGrid(s -> {
                    HostConnectLogVO vo = HostConnectLogConvert.MAPPER.to(s);
                    vo.setExtra(JSON.parseObject(s.getExtraInfo(), HostConnectLogExtraDTO.class));
                    return vo;
                });
    }

    @Override
    public List<HostConnectLogVO> getHostConnectSessions(HostConnectLogQueryRequest request) {
        // 查询全部
        List<Long> idList = hostTerminalManager.getChannelSessions()
                .values()
                .stream()
                .map(ConcurrentHashMap::values)
                .flatMap(Collection::stream)
                .filter(s -> !s.isClosed())
                .map(ITerminalSession::getConfig)
                .filter(Objects::nonNull)
                .map(TerminalConfig::getLogId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (idList.isEmpty()) {
            return Lists.empty();
        }
        // 条件
        request.setIdList(idList);
        request.setStatus(HostConnectStatusEnum.CONNECTING.name());
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request)
                .orderByDesc(HostConnectLogDO::getId);
        // 查询
        return hostConnectLogDAO.of(wrapper)
                .list(s -> {
                    HostConnectLogVO vo = HostConnectLogConvert.MAPPER.to(s);
                    vo.setExtra(JSON.parseObject(s.getExtraInfo(), HostConnectLogExtraDTO.class));
                    return vo;
                });
    }

    @Override
    public Integer updateStatusById(Long id, HostConnectStatusEnum status, Map<String, Object> partial) {
        log.info("HostConnectLogService-updateStatusById start id: {}, status: {}", id, status);
        // 查询
        HostConnectLogDO record = hostConnectLogDAO.of()
                .createWrapper()
                .eq(HostConnectLogDO::getId, id)
                .orderByDesc(HostConnectLogDO::getId)
                .then()
                .getOne();
        if (record == null) {
            log.info("HostConnectLogService-updateStatusById no record id: {}", id);
            return Const.N_0;
        }
        return this.updateStatus(record, status, partial);
    }

    /**
     * 更新状态
     *
     * @param record  record
     * @param status  status
     * @param partial partial
     * @return effect
     */
    private int updateStatus(HostConnectLogDO record, HostConnectStatusEnum status, Map<String, Object> partial) {
        // 更新
        HostConnectLogDO update = new HostConnectLogDO();
        update.setId(record.getId());
        update.setStatus(status.name());
        update.setEndTime(new Date());
        if (partial != null) {
            Map<String, Object> extra = JSON.parseObject(record.getExtraInfo());
            if (extra == null) {
                extra = partial;
            } else {
                extra.putAll(partial);
            }
            update.setExtraInfo(JSON.toJSONString(extra));
        }
        return hostConnectLogDAO.updateById(update);
    }

    @Override
    public List<Long> getLatestConnectHostId(HostConnectLogQueryRequest request) {
        return hostConnectLogDAO.selectLatestConnectHostId(SecurityUtils.getLoginUserId(), request.getType(), request.getLimit());
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<Long>> getLatestConnectHostIdAsync(HostConnectTypeEnum type, Long userId) {
        List<Long> hostIdList = hostConnectLogDAO.selectLatestConnectHostId(userId, type.name(), Const.N_10);
        return CompletableFuture.completedFuture(hostIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteHostConnectLog(List<Long> idList) {
        log.info("HostConnectLogService.deleteHostConnectLog start {}", JSON.toJSONString(idList));
        if (Lists.isEmpty(idList)) {
            OperatorLogs.add(OperatorLogs.COUNT, Const.N_0);
            return Const.N_0;
        }
        // 删除日志表
        int effect = hostConnectLogDAO.deleteBatchIds(idList);
        log.info("HostConnectLogService.deleteHostConnectLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public Long getHostConnectLogCount(HostConnectLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostConnectLogDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearHostConnectLog(HostConnectLogClearRequest request) {
        log.info("HostConnectLogService.clearHostConnectLog start {}", JSON.toJSONString(request));
        // 查询
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request)
                .select(HostConnectLogDO::getId)
                .orderByAsc(HostConnectLogDO::getId)
                .last(SqlUtils.limit(request.getLimit()));
        List<HostConnectLogDO> list = hostConnectLogDAO.selectList(wrapper);
        if (list.isEmpty()) {
            log.info("HostConnectLogService.clearHostConnectLog empty");
            // 设置日志参数
            OperatorLogs.add(OperatorLogs.COUNT, Const.N_0);
            return Const.N_0;
        }
        // 删除
        List<Long> idList = list.stream()
                .map(HostConnectLogDO::getId)
                .collect(Collectors.toList());
        return this.deleteHostConnectLog(idList);
    }

    @Override
    public Integer forceOffline(HostConnectLogQueryRequest request) {
        Long id = request.getId();
        // 查询数据是否存在
        HostConnectLogDO record = hostConnectLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.LOG_ABSENT);
        Valid.eq(record.getStatus(), HostConnectStatusEnum.CONNECTING.name(), ErrorMessage.ILLEGAL_STATUS);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.HOST_NAME, record.getHostName());
        // 获取会话
        HostConnectLogExtraDTO extra = JSON.parseObject(record.getExtraInfo(), HostConnectLogExtraDTO.class);
        ITerminalSession session = hostTerminalManager.getSession(extra.getChannelId(), extra.getSessionId());
        if (session != null) {
            // 关闭会话
            session.forceOffline();
        }
        // 更新状态
        return this.updateStatus(record, HostConnectStatusEnum.FORCE_OFFLINE, null);
    }

    @Override
    public LambdaQueryWrapper<HostConnectLogDO> buildQueryWrapper(HostConnectLogQueryRequest request) {
        return hostConnectLogDAO.wrapper()
                .eq(HostConnectLogDO::getId, request.getId())
                .in(HostConnectLogDO::getId, request.getIdList())
                .eq(HostConnectLogDO::getUserId, request.getUserId())
                .eq(HostConnectLogDO::getHostId, request.getHostId())
                .like(HostConnectLogDO::getHostAddress, request.getHostAddress())
                .eq(HostConnectLogDO::getType, request.getType())
                .like(HostConnectLogDO::getToken, request.getToken())
                .eq(HostConnectLogDO::getStatus, request.getStatus())
                .in(HostConnectLogDO::getStatus, request.getStatusList())
                .ge(HostConnectLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(HostConnectLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .le(HostConnectLogDO::getCreateTime, request.getCreateTimeLe());
    }

}
