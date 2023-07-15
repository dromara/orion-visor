package com.orion.ops.module.infra.service;

import java.util.List;

/**
 * 权限服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:03
 */
public interface PermissionService {

    /**
     * 检查角色是否含有此角色 (有效性判断)
     *
     * @param roles roles
     * @param role  role
     * @return 是否包含
     */
    boolean rolesHasRole(List<String> roles, String role);

    /**
     * 检查角色是否含有此权限 (有效性判断)
     *
     * @param roles      roles
     * @param permission permission
     * @return 是否包含
     */
    boolean rolesHasPermission(List<String> roles, String permission);

}
