/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
