package com.orion.ops.module.infra.api;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogDTO;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogQueryDTO;

/**
 * 操作日志服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:11
 */
public interface OperatorLogApi {

    /**
     * 操作日志服务
     *
     * @param request request
     * @return rows
     */
    DataGrid<OperatorLogDTO> getOperatorLogList(OperatorLogQueryDTO request);

}
