package com.orion.visor.framework.security.core.strategy;

import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 自定义安全策略配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 12:58
 */
public abstract class AuthorizeRequestsCustomizer
        implements Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry>, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

}
