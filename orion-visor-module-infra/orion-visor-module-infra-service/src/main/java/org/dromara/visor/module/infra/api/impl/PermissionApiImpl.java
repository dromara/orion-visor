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
package org.dromara.visor.module.infra.api.impl;

import org.dromara.visor.module.infra.api.PermissionApi;
import org.dromara.visor.module.infra.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限 对外服务类实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/19 15:25
 */
@Service
public class PermissionApiImpl implements PermissionApi {

    @Resource
    private PermissionService permissionService;

    @Override
    public boolean isAdminUser(Long id) {
        return permissionService.isAdminUser(id);
    }

    @Override
    public boolean hasRole(Long userId, String role) {
        return permissionService.hasRole(userId, role);
    }

    @Override
    public boolean hasAnyRole(Long userId, List<String> roles) {
        return permissionService.hasAnyRole(userId, roles);
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        return permissionService.hasPermission(userId, permission);
    }

    @Override
    public boolean hasAnyPermission(Long userId, List<String> permissions) {
        return permissionService.hasAnyPermission(userId, permissions);
    }

}
