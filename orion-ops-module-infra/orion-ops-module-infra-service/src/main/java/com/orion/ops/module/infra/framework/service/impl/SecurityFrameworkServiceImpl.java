package com.orion.ops.module.infra.framework.service.impl;

import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.framework.security.core.service.SecurityFrameworkService;
import com.orion.ops.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.enums.LoginTokenStatusEnum;
import com.orion.ops.module.infra.enums.UserStatusEnum;
import com.orion.ops.module.infra.service.AuthenticationService;
import com.orion.ops.module.infra.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 安全包 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 10:57
 */
@Service
public class SecurityFrameworkServiceImpl implements SecurityFrameworkService {

    @Resource
    private AuthenticationService authenticationService;

    @Resource
    private PermissionService permissionService;

    @Override
    public boolean hasPermission(String permission) {
        // 检查是否有权限
        return permissionService.hasPermission(permission);
    }

    @Override
    public boolean hasAnyPermission(String... permissions) {
        // 检查是否有权限
        return permissionService.hasAnyPermission(permissions);
    }

    @Override
    public boolean hasRole(String role) {
        // 检查是否有角色
        return permissionService.hasRole(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        // 检查是否有角色
        return permissionService.hasAnyRole(roles);
    }

    @Override
    public LoginUser getUserByToken(String token) {
        // 获取 token 信息
        LoginTokenDTO tokenInfo = authenticationService.getLoginTokenInfo(token, true);
        if (tokenInfo == null) {
            return null;
        }
        try {
            // 检查 token 状态
            this.checkTokenStatus(tokenInfo);
        } catch (Exception e) {
            // token 失效则删除
            RedisUtils.delete(UserCacheKeyDefine.LOGIN_TOKEN.format(tokenInfo.getId(), tokenInfo.getOrigin().getLoginTime()));
            throw e;
        }
        // 获取登录信息
        LoginUser user = authenticationService.getLoginUser(tokenInfo.getId());
        // 检查用户状态
        UserStatusEnum.checkUserStatus(user.getStatus());
        // 设置登录时间戳
        user.setTimestamp(tokenInfo.getOrigin().getLoginTime());
        return user;
    }

    /**
     * 检查 token 状态
     *
     * @param loginToken loginToken
     */
    private void checkTokenStatus(LoginTokenDTO loginToken) {
        LoginTokenStatusEnum status = LoginTokenStatusEnum.of(loginToken.getStatus());
        // 正常状态
        if (LoginTokenStatusEnum.OK.equals(status)) {
            return;
        }
        // 其他设备登录
        RuntimeException ex = status.toException(loginToken);
        if (ex != null) {
            throw ex;
        }
    }

}
