package com.orion.ops.framework.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.FilterOrderConst;
import com.orion.ops.framework.common.filter.FilterCreator;
import com.orion.ops.framework.web.core.convert.CustomerFastJsonHttpMessageConverter;
import com.orion.ops.framework.web.core.convert.SerializeConfig;
import com.orion.ops.framework.web.core.filter.TraceIdFilter;
import com.orion.ops.framework.web.core.handler.GlobalExceptionHandler;
import com.orion.ops.framework.web.core.handler.WrapperResultHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
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
@EnableConfigurationProperties(SerializeConfig.class)
public class OrionWebAutoConfiguration implements WebMvcConfigurer {

    @Value("${orion.api.prefix}")
    private String orionApiPrefix;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 公共 api 前缀
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(orionApiPrefix, clazz -> clazz.isAnnotationPresent(RestController.class)
                && antPathMatcher.match("com.orion.ops.**.controller.**", clazz.getPackage().getName()));
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
     * @param serializeConfig 序列化配置
     * @return http message fast json 转换器
     */
    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(SerializeConfig serializeConfig) {
        // json 转换器
        CustomerFastJsonHttpMessageConverter converter = new CustomerFastJsonHttpMessageConverter(serializeConfig);
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
        config.setCharset(StandardCharsets.UTF_8);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        return converter;
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
                new MediaType("application", "vnd.spring-boot.actuator.v2+json")
        );
        converter.setSupportedMediaTypes(mediaTypes);
        ObjectMapper objectMapper = converter.getObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化配置
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        return converter;
    }

    /**
     * @return http message 转换器列表
     */
    @Bean
    public HttpMessageConverters httpMessageConverters(FastJsonHttpMessageConverter fastJsonConverter,
                                                       MappingJackson2HttpMessageConverter jacksonConvert) {
        List<HttpMessageConverter<?>> defaultConverters = new HttpMessageConverters().getConverters();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        // 将 byte converter 添加至首位 - fix swagger api 返回base64报错
        converters.add(new ByteArrayHttpMessageConverter());
        // 添加自定义 converter - using WrapperResultHandler/脱敏
        converters.add(fastJsonConverter);
        // 添加自定义 converter - jackson
        converters.add(jacksonConvert);
        // 添加默认处理器
        converters.addAll(defaultConverters);
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
