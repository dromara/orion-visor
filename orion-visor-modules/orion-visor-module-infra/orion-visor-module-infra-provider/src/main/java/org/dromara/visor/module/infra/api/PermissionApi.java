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

import java.util.List;

/**
 * 权限 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/19 15:22
 */
public interface PermissionApi {

    /**
     * 用户是否为管理员用户
     *
     * @param id id
     * @return isAdmin
     */
    boolean isAdminUser(Long id);

    /**
     * 检查当前用户是否含有此角色
     *
     * @param userId userId
     * @param role   role
     * @return 是否包含
     */
    boolean hasRole(Long userId, String role);

    /**
     * 检查当前用户是否含有任意角色
     *
     * @param userId userId
     * @param roles  roles
     * @return 是否包含
     */
    boolean hasAnyRole(Long userId, List<String> roles);

    /**
     * 检查当前用户是否含有此权限
     *
     * @param userId     userId
     * @param permission permission
     * @return 是否包含
     */
    boolean hasPermission(Long userId, String permission);

    /**
     * 检查当前用户是否含任意权限
     *
     * @param userId      userId
     * @param permissions permissions
     * @return 是否包含
     */
    boolean hasAnyPermission(Long userId, List<String> permissions);

}
