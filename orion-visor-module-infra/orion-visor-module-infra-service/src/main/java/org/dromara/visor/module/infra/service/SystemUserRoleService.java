/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import org.dromara.visor.module.infra.entity.request.user.SystemUserUpdateRoleRequest;

import java.util.List;

/**
 * 用户角色关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
public interface SystemUserRoleService {

    /**
     * 通过 userId 查询 roleId
     *
     * @param userId userId
     * @return roleId
     */
    List<Long> getRoleIdListByUserId(Long userId);

    /**
     * 通过 roleCode 查询 userId
     *
     * @param roleCode roleCode
     * @return userId
     */
    List<Long> getUserIdListByRoleCode(String roleCode);

    /**
     * 删除用户角色
     *
     * @param request request
     * @return effect
     */
    Integer deleteUserRoles(SystemUserUpdateRoleRequest request);

    /**
     * 更新用户角色
     *
     * @param request request
     * @return effect
     */
    Integer updateUserRoles(SystemUserUpdateRoleRequest request);

    /**
     * 删除用户缓存中的角色
     *
     * @param roleId     roleId
     * @param userIdList userIdList
     */
    void deleteUserCacheRoleAsync(Long roleId, List<Long> userIdList);

}
