package com.orion.ops.module.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.module.asset.convert.HostConnectLogConvert;
import com.orion.ops.module.asset.dao.HostConnectLogDAO;
import com.orion.ops.module.asset.entity.domain.HostConnectLogDO;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostConnectLogVO;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.service.HostConnectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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

    @Override
    public void create(HostConnectTypeEnum type, HostConnectLogCreateRequest request) {
        HostConnectLogDO record = HostConnectLogConvert.MAPPER.to(request);
        record.setType(type.name());
        record.setStatus(HostConnectStatusEnum.CONNECTING.name());
        record.setStartTime(new Date());
        hostConnectLogDAO.insert(record);
    }

    @Override
    public DataGrid<HostConnectLogVO> getHostConnectLogPage(HostConnectLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostConnectLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostConnectLogDAO.of(wrapper)
                .page(request)
                .dataGrid(HostConnectLogConvert.MAPPER::to);
    }

    @Override
    public void updateStatusByToken(String token, HostConnectStatusEnum status) {
        log.info("HostConnectLogService-updateStatusByToken token: {}, status: {}", token, status);
        HostConnectLogDO update = new HostConnectLogDO();
        update.setStatus(status.name());
        update.setEndTime(new Date());
        hostConnectLogDAO.update(update, Conditions.eq(HostConnectLogDO::getToken, token));
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostConnectLogDO> buildQueryWrapper(HostConnectLogQueryRequest request) {
        return hostConnectLogDAO.wrapper()
                .eq(HostConnectLogDO::getUserId, request.getUserId())
                .eq(HostConnectLogDO::getHostId, request.getHostId())
                .like(HostConnectLogDO::getHostAddress, request.getHostAddress())
                .eq(HostConnectLogDO::getType, request.getType())
                .like(HostConnectLogDO::getToken, request.getToken())
                .eq(HostConnectLogDO::getStatus, request.getStatus())
                .ge(HostConnectLogDO::getStartTime, request.getStartTimeStart())
                .le(HostConnectLogDO::getStartTime, request.getStartTimeEnd());
    }

}
