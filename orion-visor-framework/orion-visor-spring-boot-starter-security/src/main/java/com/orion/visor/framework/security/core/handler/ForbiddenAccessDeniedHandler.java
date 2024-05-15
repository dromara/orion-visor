package com.orion.visor.framework.security.core.handler;

import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足处理器
 * <p>
 * {@code @PreAuthorize("@ss.has('xxx')") } 返回 false 会进入此处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 16:01
 */
@Slf4j
public class ForbiddenAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.warn("AccessDeniedHandlerImpl-handle-forbidden {} {}", SecurityUtils.getLoginUserId(), request.getRequestURI());
        Servlets.writeHttpWrapper(response, ErrorCode.FORBIDDEN.getWrapper());
    }

}
