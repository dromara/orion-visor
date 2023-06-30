package com.orion.ops.framework.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.FilterOrderConst;
import com.orion.ops.framework.common.filter.FilterCreator;
import com.orion.ops.framework.web.core.filter.TraceIdFilter;
import com.orion.ops.framework.web.core.handler.GlobalExceptionHandler;
import com.orion.ops.framework.web.core.handler.WrapperResultHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * web 配置类
 * <p>
 * TODO XSS 后续选择性的配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 16:26
 */
@AutoConfiguration
public class OrionWebAutoConfiguration implements WebMvcConfigurer {

    @Value("${orion.api.prefix}")
    private String orionApiPrefix;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 公共 api 前缀
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(orionApiPrefix, clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match("com.orion.ops.**.controller.**", clazz.getPackage().getName())); // 仅仅匹配 controller 包
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
     * @return http message json 转换器
     */
    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        // json 转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 配置
        FastJsonConfig config = new FastJsonConfig();
        // 支持的类型
        List<MediaType> mediaTypes = Lists.of(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_XHTML_XML,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.TEXT_XML
        );
        converter.setSupportedMediaTypes(mediaTypes);
        // 序列化配置
        config.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.IgnoreNonFieldGetter
        );
        converter.setFastJsonConfig(config);
        return converter;
    }

    /**
     * @return http message 转换器列表
     */
    @Bean
    public HttpMessageConverters httpMessageConverters(FastJsonHttpMessageConverter jsonConverter) {
        // 先获取默认转换器
        List<HttpMessageConverter<?>> defaultConverters = new HttpMessageConverters().getConverters();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        // 将 byte converter 添加至首位 - fix swagger api 返回base64报错
        converters.add(new ByteArrayHttpMessageConverter());
        // 添加自定义 converter - using WrapperResultHandler
        converters.add(jsonConverter);
        // 添加默认 converter
        converters.addAll(defaultConverters);
        // 设置不添加默认 converter
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
        return FilterCreator.create(new CorsFilter(source), FilterOrderConst.CORS_FILTER);
    }

    /**
     * @return traceId 配置
     */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterBean() {
        return FilterCreator.create(new TraceIdFilter(), FilterOrderConst.TRICE_ID_FILTER);
    }

}
