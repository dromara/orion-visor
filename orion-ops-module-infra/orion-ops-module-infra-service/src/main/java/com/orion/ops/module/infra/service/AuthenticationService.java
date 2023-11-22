package com.orion.ops.module.infra.service;

import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.entity.request.user.UserLoginRequest;
import com.orion.ops.module.infra.entity.vo.UserLoginVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 22:15
 */
public interface AuthenticationService {

    // TODO 配置化
    // 允许多端登录
    boolean allowMultiDevice = true;
    // 允许凭证续签
    boolean allowRefresh = true;
    // 凭证续签最大次数
    int maxRefreshCount = 3;
    // 失败锁定次数
    int maxFailedLoginCount = 5;

    /**
     * 登录
     *
     * @param request        request
     * @param servletRequest servletRequest
     * @return login
     */
    UserLoginVO login(UserLoginRequest request, HttpServletRequest servletRequest);

    /**
     * 登出
     *
     * @param servletRequest servletRequest
     */
    void logout(HttpServletRequest servletRequest);

    /**
     * 获取登录用户信息
     *
     * @param userId userId
     * @return loginUser
     */
    LoginUser getLoginUser(Long userId);

    /**
     * 获取 token 信息
     *
     * @param loginToken loginToken
     * @return tokenInfo
     */
    LoginTokenDTO getLoginTokenInfo(String loginToken);

}
