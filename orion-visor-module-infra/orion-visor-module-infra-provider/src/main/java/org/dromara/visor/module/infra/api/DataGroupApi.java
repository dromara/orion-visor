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

import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;

import java.util.List;

/**
 * 数据分组 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupApi {

    /**
     * 创建数据分组
     *
     * @param type type
     * @param dto  dto
     * @return id
     */
    Long createDataGroup(DataGroupTypeEnum type, DataGroupCreateDTO dto);

    /**
     * 重命名分组
     *
     * @param dto dto
     * @return effect
     */
    Integer renameDataGroup(DataGroupRenameDTO dto);

    /**
     * 移动分组
     *
     * @param dto dto
     * @return effect
     */
    Integer moveDataGroup(DataGroupMoveDTO dto);

    /**
     * 通过缓存查询数据分组
     *
     * @param type type
     * @return rows
     */
    List<DataGroupDTO> getDataGroupList(DataGroupTypeEnum type);

    /**
     * 通过缓存查询数据分组
     *
     * @param type type
     * @return rows
     */
    List<DataGroupDTO> getDataGroupTree(DataGroupTypeEnum type);

    /**
     * 通过 id 查询
     *
     * @param idList idList
     * @return rows
     */
    List<DataGroupDTO> getByIdList(List<Long> idList);

    /**
     * 删除数据分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupById(Long id);

    /**
     * 删除数据分组
     *
     * @param type   type
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(DataGroupTypeEnum type, List<Long> idList);

}
