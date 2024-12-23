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
package org.dromara.visor.framework.security.configuration;

import org.dromara.visor.framework.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.security.configuration.config.SecurityConfig;
import org.dromara.visor.framework.security.core.context.TransmittableThreadLocalSecurityContextHolderStrategy;
import org.dromara.visor.framework.security.core.filter.TokenAuthenticationFilter;
import org.dromara.visor.framework.security.core.handler.AuthenticationEntryPointHandler;
import org.dromara.visor.framework.security.core.handler.ForbiddenAccessDeniedHandler;
import org.dromara.visor.framework.security.core.service.SecurityFrameworkService;
import org.dromara.visor.framework.security.core.service.SecurityFrameworkServiceDelegate;
import org.dromara.visor.framework.security.core.service.SecurityHolderDelegate;
import org.dromara.visor.framework.security.core.strategy.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目安全配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 15:05
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_SECURITY)
@EnableConfigurationProperties(SecurityConfig.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class OrionSecurityAutoConfiguration {

    @Resource
    private SecurityConfig securityConfig;

    /**
     * @return 认证失败处理器
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointHandler();
    }

    /**
     * @return 权限不足处理器
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenAccessDeniedHandler();
    }

    /**
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityConfig.getPasswordEncoderLength());
    }

    /**
     * AuthenticationManager 不是bean
     * 重写父类方法可注入 AuthenticationManager
     *
     * @param authenticationConfiguration configuration
     * @return AuthenticationManagerBean
     * @throws Exception Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 声明调用 {@link SecurityContextHolder#setStrategyName(String)} 方法
     * 设置使用 {@link TransmittableThreadLocalSecurityContextHolderStrategy} 作为 Security 的上下文策略
     *
     * @return 替换策略
     */
    @Bean
    public MethodInvokingFactoryBean securityContextHolderMethodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(TransmittableThreadLocalSecurityContextHolderStrategy.class.getName());
        return methodInvokingFactoryBean;
    }

    /**
     * @param impl impl
     * @return 安全框架服务
     */
    @Bean("ss")
    @Primary
    @ConditionalOnBean(SecurityFrameworkService.class)
    public SecurityFrameworkServiceDelegate securityFrameworkService(SecurityFrameworkService impl) {
        return new SecurityFrameworkServiceDelegate(impl);
    }

    /**
     * @param delegate delegate
     * @return token 认证过滤器
     */
    @Bean
    @ConditionalOnBean(SecurityFrameworkServiceDelegate.class)
    public TokenAuthenticationFilter authenticationTokenFilter(SecurityFrameworkServiceDelegate delegate) {
        return new TokenAuthenticationFilter(delegate);
    }

    /**
     * - mybatis fill
     * - operator log
     * - log printer
     *
     * @return security holder 代理用于内部 framework 调用
     */
    @Bean
    public SecurityHolderDelegate securityHolder() {
        return new SecurityHolderDelegate();
    }

    /**
     * @return 静态资源安全策略
     */
    @Bean
    public StaticResourceAuthorizeRequestsCustomizer staticResourceAuthorizeRequestsCustomizer() {
        return new StaticResourceAuthorizeRequestsCustomizer();
    }

    /**
     * @param applicationContext applicationContext
     * @return 匿名接口安全策略
     */
    @Bean
    public PermitAllAnnotationAuthorizeRequestsCustomizer permitAllAnnotationAuthorizeRequestsCustomizer(ApplicationContext applicationContext) {
        return new PermitAllAnnotationAuthorizeRequestsCustomizer(applicationContext);
    }

    /**
     * @return 配置文件安全策略
     */
    @Bean
    public ConfigAuthorizeRequestsCustomizer configAuthorizeRequestsCustomizer() {
        return new ConfigAuthorizeRequestsCustomizer(securityConfig);
    }

    /**
     * @return websocket 安全策略
     */
    @Bean
    @ConditionalOnProperty(value = "orion.websocket.prefix")
    public WebsocketAuthorizeRequestsCustomizer websocketAuthorizeRequestsCustomizer(@Value("${orion.websocket.prefix}") String prefix) {
        return new WebsocketAuthorizeRequestsCustomizer(prefix);
    }

    /**
     * @param adminSeverContextPath adminSeverContextPath
     * @param managementEndpoints   managementEndpoints
     * @return 控制台安全策略
     */
    @Bean
    public ConsoleAuthorizeRequestsCustomizer consoleAuthorizeRequestsCustomizer(@Value("${spring.boot.admin.context-path:''}") String adminSeverContextPath,
                                                                                 @Value("${management.endpoints.web.base-path:''}") String managementEndpoints) {
        return new ConsoleAuthorizeRequestsCustomizer(adminSeverContextPath, managementEndpoints);
    }

    /**
     * 配置安全配置
     * <p>
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl 表达式结果为 true 时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数, 参数表示权限, 则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数, 参数表示角色, 则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数, 参数表示权限, 则其权限可以访问
     * hasIpAddress        |   如果有参数, 参数表示IP地址, 如果用户IP和参数匹配, 则可以访问
     * hasRole             |   如果有参数, 参数表示角色, 则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过 remember-me 登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(List<AuthorizeRequestsCustomizer> authorizeRequestsCustomizers,
                                              AuthenticationEntryPoint authenticationEntryPoint,
                                              AccessDeniedHandler accessDeniedHandler,
                                              TokenAuthenticationFilter authenticationTokenFilter,
                                              HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 开启跨域
                .cors().and()
                // 因为不使用session 禁用CSRF
                .csrf().disable()
                // 基于 token 机制所以不需要 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 不设置响应报头
                .headers().frameOptions().disable().and()
                // 认证失败处理器
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                // 权限不足处理器
                .accessDeniedHandler(accessDeniedHandler).and()
                // 设置请求权限策略
                .authorizeRequests(registry -> authorizeRequestsCustomizers.forEach(customizer -> customizer.customize(registry)))
                // 兜底规则 必须认证
                .authorizeRequests()
                .anyRequest()
                .authenticated().and()
                // 在密码认证器之前添加 token 过滤器
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
