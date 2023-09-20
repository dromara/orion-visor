package com.orion.ops.framework.security.core.service;

import com.orion.ops.framework.common.security.LoginUser;

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
     * 通过 token 获取用户信息
     *
     * @param token token
     * @return user
     */
    LoginUser getUserByToken(String token);

}
