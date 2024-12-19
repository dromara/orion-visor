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
 * 权限校验服务委托类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 11:02
 */
public class SecurityFrameworkServiceDelegate implements SecurityFrameworkService {

    private final SecurityFrameworkService delegate;

    public SecurityFrameworkServiceDelegate(SecurityFrameworkService delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasPermission(String permission) {
        return delegate.hasPermission(permission);
    }

    @Override
    public boolean hasAnyPermission(String... permissions) {
        return delegate.hasAnyPermission(permissions);
    }

    @Override
    public boolean hasRole(String role) {
        return delegate.hasRole(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        return delegate.hasAnyRole(roles);
    }

    @Override
    public LoginUser getUserByToken(String token) {
        return delegate.getUserByToken(token);
    }

}
