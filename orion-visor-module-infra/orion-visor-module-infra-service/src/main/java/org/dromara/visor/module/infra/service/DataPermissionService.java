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

import org.dromara.visor.module.infra.entity.request.data.DataPermissionUpdateRequest;

import java.util.List;

/**
 * 数据权限 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
public interface DataPermissionService {

    /**
     * 添加数据权限
     *
     * @param request request
     */
    void addDataPermission(DataPermissionUpdateRequest request);

    /**
     * 更新数据权限
     *
     * @param request request
     */
    void updateDataPermission(DataPermissionUpdateRequest request);

    /**
     * 检查用户是否有权限
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return effect
     */
    boolean hasPermission(String type, Long userId, Long relId);

    /**
     * 通过 userId 查询数据权限 (不包含角色 不走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getRelIdListByUserId(String type, Long userId);

    /**
     * 通过 roleId 查询数据权限 不走缓存
     *
     * @param type   type
     * @param roleId roleId
     * @return relId
     */
    List<Long> getRelIdListByRoleId(String type, Long roleId);

    /**
     * 查询 userId 已授权的数据权限 (包含角色 走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getUserAuthorizedRelIdList(String type, Long userId);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    int deleteByRelId(String type, Long relId);

    /**
     * 通过 relId 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    int deleteByRelIdList(String type, List<Long> relIdList);

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    int deleteByUserId(Long userId);

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    int deleteByUserIdList(List<Long> userIdList);

    /**
     * 通过 roleId 删除
     *
     * @param roleId roleId
     * @return effect
     */
    int deleteByRoleId(Long roleId);

    /**
     * 清空角色缓存
     *
     * @param roleId roleId
     */
    void clearRoleCache(Long roleId);

    /**
     * 清空用户缓存
     *
     * @param userId userId
     */
    void clearUserCache(Long userId);

    /**
     * 清空用户缓存
     *
     * @param userIdList userIdList
     */
    void clearUserCache(List<Long> userIdList);

}
