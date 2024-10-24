/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.infra.define;

import java.util.Collection;

/**
 * 权限定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:39
 */
public interface RoleDefine {

    /**
     * 超级管理员权限
     */
    String ADMIN_CODE = "admin";

    /**
     * 是否为管理员角色
     *
     * @param role role
     * @return 是否为管理员
     */
    static boolean isAdmin(String role) {
        return ADMIN_CODE.equals(role);
    }

    /**
     * 是否包含管理员角色
     *
     * @param roles roles
     * @return 是否包含管理员
     */
    static boolean containsAdmin(Collection<String> roles) {
        return roles.contains(ADMIN_CODE);
    }

}
