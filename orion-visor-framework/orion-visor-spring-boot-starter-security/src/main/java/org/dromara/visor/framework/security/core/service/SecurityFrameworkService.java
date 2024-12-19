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
package org.dromara.visor.framework.security.core.service;

import org.dromara.visor.framework.common.security.LoginUser;

/**
 * 权限校验服务
 * <p>
 * 在业务层定义 bean
 * 使用 @PreAuthorize("@ss.hasPermission('xxx')")
 * <p>
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 18:25
 */
public interface SecurityFrameworkService {

    /**
     * 检查是否有权限
     *
     * @param permission 权限
     * @return has
     */
    boolean hasPermission(String permission);

    /**
     * 检查是否有任意权限
     *
     * @param permissions 权限
     * @return has
     */
    boolean hasAnyPermission(String... permissions);

    /**
     * 检查是否有角色
     *
     * @param role 角色
     * @return has
     */
    boolean hasRole(String role);

    /**
     * 检查是否有角色
     *
     * @param roles 角色
     * @return has
     */
    boolean hasAnyRole(String... roles);

    /**
     * 通过 token 获取用户信息
     *
     * @param token token
     * @return user
     */
    LoginUser getUserByToken(String token);

}
