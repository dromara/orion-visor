package com.orion.visor.framework.security.core.handler;

import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
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
