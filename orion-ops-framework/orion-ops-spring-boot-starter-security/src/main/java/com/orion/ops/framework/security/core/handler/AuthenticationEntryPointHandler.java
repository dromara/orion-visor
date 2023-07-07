package com.orion.ops.framework.security.core.handler;

import com.orion.ops.framework.common.constant.ErrorCode;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 16:01
 */
@Slf4j
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.debug("AuthenticationEntryPoint-commence-未登录 {}", request.getRequestURI(), e);
        Servlets.writeHttpWrapper(response, ErrorCode.UNAUTHORIZED.getWrapper());
    }

}
