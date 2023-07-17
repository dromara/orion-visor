package com.orion.ops.module.infra.service;

import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.entity.request.auth.UserLoginRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 22:15
 */
public interface AuthenticationService {

    /**
     * 登陆
     *
     * @param request        request
     * @param servletRequest servletRequest
     * @return token
     */
    String login(UserLoginRequest request, HttpServletRequest servletRequest);

    /**
     * 登出
     *
     * @param servletRequest servletRequest
     */
    void logout(HttpServletRequest servletRequest);

    /**
     * 获取登陆用户信息
     *
     * @param userId userId
     * @return loginUser
     */
    LoginUser getLoginUser(Long userId);

    /**
     * 获取 token 信息
     *
     * @param loginToken   loginToken
     * @param checkRefresh 是否检查 refreshToken
     * @return tokenInfo
     */
    LoginTokenDTO getLoginTokenInfo(String loginToken, boolean checkRefresh);

}
