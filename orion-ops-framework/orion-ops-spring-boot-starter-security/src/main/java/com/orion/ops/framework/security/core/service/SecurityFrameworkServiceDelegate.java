package com.orion.ops.framework.security.core.service;

import com.orion.ops.framework.common.security.LoginUser;

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
    public boolean hasRole(String role) {
        return delegate.hasRole(role);
    }

    @Override
    public LoginUser getUserByToken(String token) {
        return delegate.getUserByToken(token);
    }

}
