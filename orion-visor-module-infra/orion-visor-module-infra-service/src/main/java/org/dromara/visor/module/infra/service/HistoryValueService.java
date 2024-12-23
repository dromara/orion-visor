/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import org.dromara.visor.module.infra.entity.domain.HistoryValueDO;
import org.dromara.visor.module.infra.entity.request.history.HistoryValueCreateRequest;
import org.dromara.visor.module.infra.entity.request.history.HistoryValueQueryRequest;
import org.dromara.visor.module.infra.entity.vo.HistoryValueVO;

import java.util.List;

/**
 * 历史归档 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
public interface HistoryValueService {

    /**
     * 创建历史归档
     *
     * @param request request
     * @return id
     */
    Long createHistoryValue(HistoryValueCreateRequest request);

    /**
     * 分页查询历史归档
     *
     * @param request request
     * @return rows
     */
    DataGrid<HistoryValueVO> getHistoryValuePage(HistoryValueQueryRequest request);

    /**
     * 通过 id 查询
     *
     * @param id id
     * @return value
     */
    HistoryValueDO getHistoryById(Long id);

    /**
     * 通过 id 查询
     *
     * @param id    id
     * @param relId relId
     * @param type  type
     * @return value
     */
    HistoryValueDO getHistoryByRelId(Long id, Long relId, String type);

    /**
     * 删除历史归档
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(String type, Long relId);

    /**
     * 批量删除历史归档
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(String type, List<Long> relIdList);

}
