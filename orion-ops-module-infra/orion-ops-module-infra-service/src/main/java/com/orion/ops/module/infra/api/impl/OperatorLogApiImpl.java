package com.orion.ops.module.infra.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.OperatorLogApi;
import com.orion.ops.module.infra.convert.OperatorLogProviderConvert;
import com.orion.ops.module.infra.dao.OperatorLogDAO;
import com.orion.ops.module.infra.entity.domain.OperatorLogDO;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogDTO;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:18
 */
@Slf4j
@Service
public class OperatorLogApiImpl implements OperatorLogApi {

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Override
    public DataGrid<OperatorLogDTO> getOperatorLogPage(OperatorLogQueryDTO request) {
        Valid.valid(request);
        return operatorLogDAO.of()
                .page(request)
                .wrapper(this.buildQueryWrapper(request))
                .dataGrid(OperatorLogProviderConvert.MAPPER::to);
    }

    @Override
    public Integer deleteOperatorLog(List<Long> idList) {
        return operatorLogDAO.deleteBatchIds(idList);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<OperatorLogDO> buildQueryWrapper(OperatorLogQueryDTO request) {
        return operatorLogDAO.wrapper()
                .eq(OperatorLogDO::getId, request.getId())
                .eq(OperatorLogDO::getUserId, request.getUserId())
                .eq(OperatorLogDO::getRiskLevel, request.getRiskLevel())
                .eq(OperatorLogDO::getModule, request.getModule())
                .eq(OperatorLogDO::getType, request.getType())
                .in(OperatorLogDO::getType, request.getTypeList())
                .eq(OperatorLogDO::getResult, request.getResult())
                .like(OperatorLogDO::getExtra, request.getExtra())
                .ge(OperatorLogDO::getStartTime, request.getStartTimeStart())
                .le(OperatorLogDO::getStartTime, request.getStartTimeEnd())
                .orderByDesc(OperatorLogDO::getId);
    }

}
