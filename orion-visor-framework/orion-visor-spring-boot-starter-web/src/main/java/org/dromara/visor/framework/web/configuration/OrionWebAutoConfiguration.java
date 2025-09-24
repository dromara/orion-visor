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
package org.dromara.visor.framework.web.configuration;

import cn.orionsec.kit.lang.utils.collect.Lists;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.common.constant.FilterOrderConst;
import org.dromara.visor.common.web.WebFilterCreator;
import org.dromara.visor.framework.web.configuration.config.ExposeApiConfig;
import org.dromara.visor.framework.web.core.aspect.DemoDisableApiAspect;
import org.dromara.visor.framework.web.core.aspect.ExposeApiAspect;
import org.dromara.visor.framework.web.core.filter.TraceIdFilter;
import org.dromara.visor.framework.web.core.handler.GlobalExceptionHandler;
import org.dromara.visor.framework.web.core.handler.WrapperResultHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * web 配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 16:26
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_WEB)
@EnableConfigurationProperties(ExposeApiConfig.class)
public class OrionWebAutoConfiguration implements WebMvcConfigurer {

    @Value("${orion.prefix}")
    private String orionPrefix;

    @Value("${orion.api.prefix}")
    private String orionApiPrefix;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 公共 api 前缀
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(orionApiPrefix, clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match("org.dromara.visor.**.controller.**", clazz.getPackage().getName()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 公共模板前缀
        registry.addResourceHandler(orionPrefix + "/template/**")
                .addResourceLocations("classpath:/public/template/");
    }

    /**
     * @return 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * @return 公共返回值包装处理器
     */
    @Bean
    public WrapperResultHandler wrapperResultHandler() {
        return new WrapperResultHandler();
    }

    /**
     * @return http message jackson 转换器
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 支持的类型
        List<MediaType> mediaTypes = Lists.of(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_XHTML_XML,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.TEXT_XML,
                MediaType.ALL
        );
        converter.setSupportedMediaTypes(mediaTypes);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        // 默认 objectMapper
        ObjectMapper objectMapper = converter.getObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return converter;
    }

    /**
     * @return http message 转换器列表
     */
    @Bean
    public HttpMessageConverters httpMessageConverters(MappingJackson2HttpMessageConverter jacksonConvert) {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        // 添加 byte converter - swagger api
        converters.add(new ByteArrayHttpMessageConverter());
        // 添加 resource region - admin api log
        converters.add(new ResourceRegionHttpMessageConverter());
        // 添加 json converter - jackson
        converters.add(jacksonConvert);
        return new HttpMessageConverters(false, converters);
    }

    /**
     * @return 跨域配置
     */
    @Bean
    @ConditionalOnProperty(value = "orion.api.cors", havingValue = "true")
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        // 跨域配置
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return WebFilterCreator.create(new CorsFilter(source), FilterOrderConst.CORS_FILTER);
    }

    /**
     * @return traceId 配置
     */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterBean() {
        return WebFilterCreator.create(new TraceIdFilter(), FilterOrderConst.TRICE_ID_FILTER);
    }

    /**
     * @return 演示模式禁用 api 切面
     */
    @Bean
    @ConditionalOnProperty(value = "orion.demo", havingValue = "true")
    public DemoDisableApiAspect demoDisableApiAspect() {
        return new DemoDisableApiAspect();
    }

    /**
     * @param config config
     * @return 对外服务 api 切面
     */
    @Bean
    public ExposeApiAspect exposeApiAspect(ExposeApiConfig config) {
        return new ExposeApiAspect(config);
    }

}
