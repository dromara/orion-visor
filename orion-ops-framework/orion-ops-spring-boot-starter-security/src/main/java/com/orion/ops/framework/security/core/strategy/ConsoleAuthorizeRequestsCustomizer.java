package com.orion.ops.framework.security.core.strategy;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 控制台 认证策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 13:04
 */
public class ConsoleAuthorizeRequestsCustomizer extends AuthorizeRequestsCustomizer {

    private final String adminSeverContextPath;

    private final String managementEndpoints;

    public ConsoleAuthorizeRequestsCustomizer(String adminSeverContextPath, String managementEndpoints) {
        this.adminSeverContextPath = adminSeverContextPath;
        this.managementEndpoints = managementEndpoints;
    }

    @Override
    public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        registry
                // swagger 接口文档
                .antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**", "/webjars/**", "/*/api-docs").anonymous()
                // druid 监控
                .antMatchers("/druid/**").anonymous()
                // actuator 安全配置
                .antMatchers(managementEndpoints, managementEndpoints + "/**").anonymous()
                // admin
                .antMatchers(adminSeverContextPath, adminSeverContextPath + "/**").anonymous();
    }

}
