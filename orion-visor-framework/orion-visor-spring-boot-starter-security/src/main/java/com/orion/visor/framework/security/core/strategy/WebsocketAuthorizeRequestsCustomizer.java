package com.orion.visor.framework.security.core.strategy;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * websocket 认证策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 13:04
 */
public class WebsocketAuthorizeRequestsCustomizer extends AuthorizeRequestsCustomizer {

    private final String prefix;

    public WebsocketAuthorizeRequestsCustomizer(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        // websocket 允许匿名访问
        registry.antMatchers(prefix + "/**").permitAll();
    }

}
