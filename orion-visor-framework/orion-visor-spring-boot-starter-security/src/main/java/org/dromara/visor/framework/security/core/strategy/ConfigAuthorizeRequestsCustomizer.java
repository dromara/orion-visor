/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import org.dromara.visor.framework.security.configuration.config.SecurityConfig;
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
