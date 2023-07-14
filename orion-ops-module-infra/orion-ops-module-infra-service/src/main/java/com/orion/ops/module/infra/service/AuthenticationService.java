package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.Pair;
import com.orion.ops.module.infra.entity.request.UserLoginRequest;

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
     * 获取 token pair
     *
     * @param loginToken loginToken
     * @return pair
     */
    Pair<Long, Long> getLoginTokenPair(String loginToken);

}
