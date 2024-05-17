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
