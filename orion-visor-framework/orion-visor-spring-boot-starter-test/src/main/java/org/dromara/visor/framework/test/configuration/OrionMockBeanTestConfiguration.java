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
package org.dromara.visor.framework.test.configuration;

import cn.orionsec.kit.lang.utils.collect.Sets;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.common.security.SecurityHolder;
import org.dromara.visor.framework.test.core.utils.EntityRandoms;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import java.util.Set;

/**
 * 单元测试依赖 bean 初始化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/24 14:53
 */
@Lazy(false)
@Profile("unit-test")
@Configuration(proxyBeanMethods = false)
public class OrionMockBeanTestConfiguration {

    /**
     * 空实现数据填充器 用于单元测试 单元测试不会注入 spring-security
     *
     * @return SecurityHolder
     */
    @Bean
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

    /**
     * 空实现对象验证器 用于单元测试
     *
     * @return Validator
     */
    @Bean
    @ConditionalOnMissingBean(Validator.class)
    public Validator emptyValidator() {
        return new Validator() {
            @Override
            public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
                return Sets.empty();
            }

            @Override
            public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
                return Sets.empty();
            }

            @Override
            public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
                return Sets.empty();
            }

            @Override
            public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> type) {
                return null;
            }

            @Override
            public ExecutableValidator forExecutables() {
                return null;
            }
        };
    }

}
