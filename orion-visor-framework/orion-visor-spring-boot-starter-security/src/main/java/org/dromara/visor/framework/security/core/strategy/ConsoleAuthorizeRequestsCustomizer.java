/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.security.core.strategy;

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
