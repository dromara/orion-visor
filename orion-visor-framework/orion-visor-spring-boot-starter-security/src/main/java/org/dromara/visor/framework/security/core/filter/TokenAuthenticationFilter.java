/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.framework.security.core.filter;

import cn.orionsec.kit.lang.exception.argument.HttpWrapperException;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ErrorCode;
import org.dromara.visor.framework.common.security.LoginUser;
import org.dromara.visor.framework.security.core.service.SecurityFrameworkService;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器
 * 验证 token 有效后将其加入上下文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 18:39
 */
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityFrameworkService securityFrameworkService;

    public TokenAuthenticationFilter(SecurityFrameworkService securityFrameworkService) {
        this.securityFrameworkService = securityFrameworkService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // 获取请求头 token
            String token = SecurityUtils.obtainAuthorization(request);
            if (!Strings.isBlank(token)) {
                // 通过 token 获取用户信息
                LoginUser loginUser = securityFrameworkService.getUserByToken(token);
                // 设置用户上下文
                if (loginUser != null) {
                    SecurityUtils.setLoginUser(loginUser, request);
                }
            }
        } catch (HttpWrapperException e) {
            log.error("TokenAuthenticationFilter.doFilterInternal auth error {}", e.getWrapper().getMsg());
            Servlets.writeHttpWrapper(response, e.getWrapper());
            return;
        } catch (Exception e) {
            log.error("TokenAuthenticationFilter.doFilterInternal parser error", e);
            Servlets.writeHttpWrapper(response, ErrorCode.INTERNAL_SERVER_ERROR.getWrapper());
            return;
        }
        // 继续执行
        chain.doFilter(request, response);
    }

}
