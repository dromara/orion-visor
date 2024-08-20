package com.orion.visor.module.infra.api.impl;

import com.orion.visor.module.infra.api.PermissionApi;
import com.orion.visor.module.infra.service.PermissionService;
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
