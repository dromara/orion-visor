package com.orion.visor.module.infra.api;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.infra.entity.dto.operator.OperatorLogDTO;
import com.orion.visor.module.infra.entity.dto.operator.OperatorLogQueryDTO;

import java.util.List;

/**
 * 操作日志服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:11
 */
public interface OperatorLogApi {

    /**
     * 分页查询操作日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<OperatorLogDTO> getOperatorLogPage(OperatorLogQueryDTO request);

    /**
     * 删除操作日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteOperatorLog(List<Long> idList);

}
