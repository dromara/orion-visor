package com.orion.visor.framework.security.core.strategy;

import com.orion.visor.framework.security.configuration.config.SecurityConfig;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 配置文件 认证策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 13:04
 */
public class ConfigAuthorizeRequestsCustomizer extends AuthorizeRequestsCustomizer {

    private final SecurityConfig securityConfig;

    public ConfigAuthorizeRequestsCustomizer(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    @Override
    public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        // 配置文件 无需认证
        registry.antMatchers(securityConfig.getPermitUrl().toArray(new String[0])).permitAll();
    }

}
