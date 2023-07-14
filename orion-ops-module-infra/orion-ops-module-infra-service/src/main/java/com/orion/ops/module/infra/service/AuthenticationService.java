package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.UserLoginRequest;

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
     * @param request request
     * @return token
     */
    String login(UserLoginRequest request);

    /**
     * 登出
     *
     * @param token token
     */
    void logout(String token);

}
