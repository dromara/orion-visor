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
package org.dromara.visor.module.infra.api;

import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;

import java.util.List;

/**
 * 数据分组用户 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupUserApi {

    /**
     * 创建数据分组
     *
     * @param type   type
     * @param userId userId
     * @param dto    dto
     * @return id
     */
    Long createDataGroup(DataGroupTypeEnum type, Long userId, DataGroupCreateDTO dto);

    /**
     * 通过缓存查询数据分组
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupDTO> getDataGroupList(DataGroupTypeEnum type, Long userId);

    /**
     * 通过缓存查询数据分组
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupDTO> getDataGroupTree(DataGroupTypeEnum type, Long userId);

    /**
     * 删除数据分组
     *
     * @param type   type
     * @param userId userId
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(DataGroupTypeEnum type, Long userId, List<Long> idList);

}
