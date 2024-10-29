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
package org.dromara.visor.framework.security.core.handler;

import cn.orionsec.kit.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未认证处理器
 * <p>
 * 过滤器执行完还未设置用户上下文则会进入此处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 16:01
 */
@Slf4j
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.debug("AuthenticationEntryPoint-commence-unauthorized {}", request.getRequestURI(), e);
        Servlets.writeHttpWrapper(response, ErrorCode.UNAUTHORIZED.getWrapper());
    }

}
