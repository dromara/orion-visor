/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.service;

import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.dto.LoginTokenDTO;
import org.dromara.visor.module.infra.entity.request.user.UserLoginRequest;
import org.dromara.visor.module.infra.entity.vo.UserLoginVO;

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

    /**
     * 登录预检查
     *
     * @param username username
     * @param password password
     * @return user
     */
    SystemUserDO preCheckLogin(String username, String password);

    /**
     * 检查用户密码
     *
     * @param user           user
     * @param password       password
     * @param addFailedCount addFailedCount
     * @return passRight
     */
    boolean checkUserPassword(SystemUserDO user, String password, boolean addFailedCount);

    /**
     * 检查用户状态
     *
     * @param user user
     */
    void checkUserStatus(SystemUserDO user);

}
