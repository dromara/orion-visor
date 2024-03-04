package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.constant.Const;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Valid;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.HostConnectLogConvert;
import com.orion.ops.module.asset.dao.HostConnectLogDAO;
import com.orion.ops.module.asset.entity.domain.HostConnectLogDO;
import com.orion.ops.module.asset.entity.dto.HostConnectLogExtraDTO;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostConnectLogVO;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.ops.module.asset.service.HostConnectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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
    private TerminalManager terminalManager;

    @Override
    public void create(HostConnectTypeEnum type, HostConnectLogCreateRequest request) {
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
    }

    @Override
    public DataGrid<HostConnectLogVO> getHostConnectLogPage(HostConnectLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostConnectLogDAO.of(wrapper)
                .page(request)
                .dataGrid(s -> {
                    HostConnectLogVO vo = HostConnectLogConvert.MAPPER.to(s);
                    vo.setExtra(JSON.parseObject(s.getExtraInfo()));
                    return vo;
                });
    }

    @Override
    public Integer updateStatusByToken(String token, HostConnectStatusEnum status) {
        log.info("HostConnectLogService-updateStatusByToken token: {}, status: {}", token, status);
        HostConnectLogDO update = new HostConnectLogDO();
        update.setStatus(status.name());
        update.setEndTime(new Date());
        return hostConnectLogDAO.update(update, Conditions.eq(HostConnectLogDO::getToken, token));
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
    public Integer deleteHostConnectLog(List<Long> idList) {
        log.info("HostConnectLogService.deleteHostConnectLog start {}", JSON.toJSONString(idList));
        int effect = hostConnectLogDAO.deleteBatchIds(idList);
        log.info("HostConnectLogService.deleteHostConnectLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public Long getHostConnectLogCount(HostConnectLogQueryRequest request) {
        return hostConnectLogDAO.selectCount(this.buildQueryWrapper(request));
    }

    @Override
    public Integer clearHostConnectLog(HostConnectLogQueryRequest request) {
        // TODO 测试一下参数
        log.info("HostConnectLogService.clearHostConnectLog start {}", JSON.toJSONString(request));
        // 删除
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request);
        int effect = hostConnectLogDAO.delete(wrapper);
        log.info("HostConnectLogService.clearHostConnectLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public Integer forceOffline(HostConnectLogQueryRequest request) {
        Long id = request.getId();
        // 查询数据是否存在
        HostConnectLogDO connect = hostConnectLogDAO.selectById(id);
        Valid.notNull(connect, ErrorMessage.LOG_ABSENT);
        Valid.eq(connect.getStatus(), HostConnectStatusEnum.CONNECTING.name(), ErrorMessage.ILLEGAL_STATUS);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.HOST_NAME, connect.getHostName());
        // 获取会话
        HostConnectLogExtraDTO extra = JSON.parseObject(connect.getExtraInfo(), HostConnectLogExtraDTO.class);
        ITerminalSession session = terminalManager.getSession(extra.getChannelId(), extra.getSessionId());
        if (session != null) {
            // 关闭会话
            session.forceOffline();
        }
        // 更新状态
        return this.updateStatusByToken(connect.getToken(), HostConnectStatusEnum.FORCE_OFFLINE);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostConnectLogDO> buildQueryWrapper(HostConnectLogQueryRequest request) {
        return hostConnectLogDAO.wrapper()
                .eq(HostConnectLogDO::getId, request.getId())
                .eq(HostConnectLogDO::getUserId, request.getUserId())
                .eq(HostConnectLogDO::getHostId, request.getHostId())
                .like(HostConnectLogDO::getHostAddress, request.getHostAddress())
                .eq(HostConnectLogDO::getType, request.getType())
                .like(HostConnectLogDO::getToken, request.getToken())
                .eq(HostConnectLogDO::getStatus, request.getStatus())
                .ge(HostConnectLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(HostConnectLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .orderByDesc(HostConnectLogDO::getId);
    }

}
