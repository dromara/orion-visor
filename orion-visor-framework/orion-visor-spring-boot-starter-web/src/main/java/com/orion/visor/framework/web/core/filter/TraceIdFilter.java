package com.orion.visor.framework.web.core.filter;

import com.orion.lang.id.UUIds;
import com.orion.visor.framework.common.meta.TraceIdHolder;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * traceId 过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 17:45
 */
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获 traceId
            String traceId = UUIds.random32();
            // 设置应用上下文
            TraceIdHolder.set(traceId);
            // 设置日志上下文
            MDC.put(TraceIdHolder.TRACE_ID_MDC, traceId);
            // 设置响应头
            response.setHeader(TraceIdHolder.TRACE_ID_HEADER, traceId);
            // 执行请求
            filterChain.doFilter(request, response);
        } finally {
            // 清理应用上下文
            TraceIdHolder.remove();
            // 清理日志上下文
            MDC.clear();
        }
    }

}
