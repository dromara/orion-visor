package com.orion.ops.framework.mybatis.cache;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * mybatis 缓存清理过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 15:14
 */
public class RowCacheClearFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 执行请求
            filterChain.doFilter(request, response);
        } finally {
            // 清理缓存
            // TODO test
            RowCacheHolder.remove();
        }
    }

}
