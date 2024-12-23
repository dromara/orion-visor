/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.api;

import org.dromara.visor.module.infra.entity.dto.history.HistoryValueCreateDTO;
import org.dromara.visor.module.infra.entity.dto.history.HistoryValueDTO;
import org.dromara.visor.module.infra.enums.HistoryValueTypeEnum;

import java.util.List;

/**
 * 历史归档 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
public interface HistoryValueApi {

    /**
     * 创建历史归档
     *
     * @param type type
     * @param dto  dto
     * @return id
     */
    Long createHistoryValue(HistoryValueTypeEnum type, HistoryValueCreateDTO dto);

    /**
     * 查询历史归档
     *
     * @param id id
     * @return row
     */
    HistoryValueDTO getHistoryValueById(Long id);

    /**
     * 查询历史归档
     *
     * @param id    id
     * @param relId relId
     * @param type  type
     * @return row
     */
    HistoryValueDTO getHistoryValueByRelId(Long id, Long relId, HistoryValueTypeEnum type);

    /**
     * 删除历史归档
     *
     * @param type  type
     * @param relId relId
     * @return rows
     */
    Integer deleteByRelId(HistoryValueTypeEnum type, Long relId);

    /**
     * 删除历史归档
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(HistoryValueTypeEnum type, List<Long> relIdList);

}
