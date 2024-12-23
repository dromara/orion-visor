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

import org.dromara.visor.module.infra.entity.dto.data.DataPermissionUpdateDTO;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;

import java.util.List;

/**
 * 数据权限 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
public interface DataPermissionApi {

    /**
     * 添加数据权限
     *
     * @param type type
     * @param dto  dto
     */
    void addDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto);

    /**
     * 更新数据权限
     *
     * @param type type
     * @param dto  dto
     */
    void updateDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto);

    /**
     * 检查用户是否有权限
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return effect
     */
    boolean hasPermission(DataPermissionTypeEnum type, Long userId, Long relId);

    /**
     * 通过 userId 查询数据权限 (不包含角色 不走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getRelIdListByUserId(DataPermissionTypeEnum type, Long userId);

    /**
     * 通过 roleId 查询数据权限 不走缓存
     *
     * @param type   type
     * @param roleId roleId
     * @return relId
     */
    List<Long> getRelIdListByRoleId(DataPermissionTypeEnum type, Long roleId);

    /**
     * 查询 userId 已授权的数据权限 (包含角色 走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getUserAuthorizedRelIdList(DataPermissionTypeEnum type, Long userId);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    int deleteByRelId(DataPermissionTypeEnum type, Long relId);

    /**
     * 通过 relId 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    int deleteByRelIdList(DataPermissionTypeEnum type, List<Long> relIdList);

}
