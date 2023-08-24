package com.orion.ops.framework.test.config;

import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.security.SecurityHolder;
import com.orion.ops.framework.test.core.utils.EntityRandoms;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

/**
 * 单元测试 security 初始化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/24 14:53
 */
@Lazy(false)
@Profile("unit-test")
@Configuration(proxyBeanMethods = false)
public class OrionSecurityTestConfiguration {

    /**
     * 空实现数据填充器 用于单元测试 单元测试不会注入 spring-security
     *
     * @return SecurityHolder
     */
    @Bean
    @Profile("unit-test")
    @ConditionalOnMissingBean(SecurityHolder.class)
    public SecurityHolder emptySecurityHolder() {
        return new SecurityHolder() {

            private final LoginUser DEFAULT = EntityRandoms.random(LoginUser.class);

            @Override
            public LoginUser getLoginUser() {
                return DEFAULT;
            }

            @Override
            public Long getLoginUserId() {
                return DEFAULT.getId();
            }
        };
    }

}
