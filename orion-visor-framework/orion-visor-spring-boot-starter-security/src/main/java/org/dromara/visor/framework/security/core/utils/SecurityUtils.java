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
package org.dromara.visor.framework.security.core.utils;

import cn.orionsec.kit.lang.constant.StandardHttpHeader;
import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.security.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * 安全工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 11:13
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取 token
     *
     * @param request request
     * @return token
     */
    public static String obtainAuthorization(HttpServletRequest request) {
        String authorization = request.getHeader(StandardHttpHeader.AUTHORIZATION);
        if (Strings.isEmpty(authorization)) {
            return null;
        }
        if (!authorization.contains(Const.BEARER) || authorization.length() <= Const.BEARER_PREFIX_LEN) {
            return null;
        }
        return authorization.substring(Const.BEARER_PREFIX_LEN).trim();
    }

    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication.getPrincipal() : null;
    }

    /**
     * 获取当前 userId
     *
     * @return id
     */
    public static Long getLoginUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 获取当前 username
     *
     * @return username
     */
    public static String getLoginUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    /**
     * 获取当前 timestamp
     *
     * @return timestamp
     */
    public static Long getLoginTimestamp() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getTimestamp() : null;
    }

    /**
     * 清空用户上下文
     */
    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request   请求
     */
    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // 创建 authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
        if (request != null) {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        // 设置上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
