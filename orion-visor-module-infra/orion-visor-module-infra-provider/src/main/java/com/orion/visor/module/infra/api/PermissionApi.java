package com.orion.visor.module.infra.api;

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
