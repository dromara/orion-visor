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

import org.dromara.visor.module.infra.entity.dto.DataGroupCacheDTO;
import org.dromara.visor.module.infra.entity.request.data.DataGroupCreateRequest;
import org.dromara.visor.module.infra.entity.request.data.DataGroupMoveRequest;
import org.dromara.visor.module.infra.entity.request.data.DataGroupRenameRequest;

import java.util.List;

/**
 * 数据分组 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupService {

    /**
     * 创建数据分组
     *
     * @param request request
     * @return id
     */
    Long createDataGroup(DataGroupCreateRequest request);

    /**
     * 重命名分组
     *
     * @param request request
     * @return effect
     */
    Integer renameDataGroup(DataGroupRenameRequest request);

    /**
     * 移动分组
     *
     * @param request request
     * @return effect
     */
    Integer moveDataGroup(DataGroupMoveRequest request);

    /**
     * 通过缓存查询数据分组 - 列表
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupCacheDTO> getDataGroupListByCache(String type, Long userId);

    /**
     * 通过缓存查询数据分组 - 树结构
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupCacheDTO> getDataGroupTreeByCache(String type, Long userId);

    /**
     * 通过 id 删除数据分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupById(Long id);

    /**
     * 通过 id 删除数据分组
     *
     * @param type   type
     * @param userId userId
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(String type, Long userId, List<Long> idList);

    /**
     * 通过 userId 删除数据分组
     *
     * @param userId userId
     * @return effect
     */
    Integer deleteDataGroupByUserId(Long userId);

    /**
     * 通过 userId 删除数据分组
     *
     * @param userIdList userIdList
     * @return effect
     */
    Integer deleteDataGroupByUserIdList(List<Long> userIdList);

}
