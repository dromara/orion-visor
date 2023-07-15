package com.orion.ops.module.infra.define;

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
     * 是否为管理员权限
     *
     * @param role role
     * @return 是否为管理员
     */
    static boolean isAdmin(String role) {
        return ADMIN_CODE.equals(role);
    }

}
