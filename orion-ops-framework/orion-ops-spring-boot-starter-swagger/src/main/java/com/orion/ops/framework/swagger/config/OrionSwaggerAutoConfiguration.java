package com.orion.ops.framework.swagger.config;

import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.*;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * swagger 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/6/21 11:22
 */
@Profile({"dev"})
@ConditionalOnClass({OpenAPI.class})
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "springdoc.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_SWAGGER)
public class OrionSwaggerAutoConfiguration {

    @Value("${orion.api.prefix}")
    private String orionApiPrefix;

    /**
     * @param properties 配置
     * @return OpenAPI
     */
    @Bean
    public OpenAPI openApi(SwaggerProperties properties) {
        Map<String, SecurityScheme> securitySchemas = this.buildSecuritySchemes();
        OpenAPI api = new OpenAPI()
                // 接口信息
                .info(this.buildInfo(properties))
                // 接口安全配置
                .components(new Components().securitySchemes(securitySchemas));
        securitySchemas.keySet()
                .forEach(key -> api.addSecurityItem(new SecurityRequirement().addList(key)));
        return api;
    }

    /**
     * api 摘要信息
     */
    private Info buildInfo(SwaggerProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact().name(properties.getAuthor()).url(properties.getUrl()).email(properties.getEmail()))
                .license(new License().name(properties.getLicense()).url(properties.getLicenseUrl()));
    }

    /**
     * 配置请求头 Authorization 传递 token 参数
     *
     * @return 安全模式
     */
    private Map<String, SecurityScheme> buildSecuritySchemes() {
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();
        SecurityScheme securityScheme = new SecurityScheme()
                // 类型
                .type(SecurityScheme.Type.APIKEY)
                // 请求头的 name
                .name(HttpHeaders.AUTHORIZATION)
                // token 所在位置
                .in(SecurityScheme.In.HEADER);
        securitySchemes.put(HttpHeaders.AUTHORIZATION, securityScheme);
        return securitySchemes;
    }

    /**
     * @return 自定义 OpenAPI 处理器
     */
    @Bean
    public OpenAPIService openApiBuilder(Optional<OpenAPI> openAPI,
                                         SecurityService securityParser,
                                         SpringDocConfigProperties springDocConfigProperties,
                                         PropertyResolverUtils propertyResolverUtils,
                                         Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
                                         Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
                                         Optional<JavadocProvider> javadocProvider) {
        return new OpenAPIService(openAPI, securityParser, springDocConfigProperties,
                propertyResolverUtils, openApiBuilderCustomizers, serverBaseUrlCustomizers, javadocProvider);
    }

    /**
     * @param properties  properties
     * @param beanFactory beanFactory
     * @return 所有模块的 api 分组
     */
    @Bean
    public GroupedOpenApi allGroupedOpenApi(ConfigurableListableBeanFactory beanFactory,
                                            SwaggerProperties properties) {
        // 全部
        GroupedOpenApi all = buildGroupedOpenApi("全部", "*");
        // 注册模块分组 api
        properties.getGroupedApi().forEach((t, v) -> {
            GroupedOpenApi api = buildGroupedOpenApi(v.getGroup(), v.getPath());
            beanFactory.registerSingleton(t + "GroupedOpenApi", api);
        });
        return all;
    }

    /**
     * 构建 api 分组
     *
     * @param group group
     * @param path  path
     * @return group
     */
    private GroupedOpenApi buildGroupedOpenApi(String group, String path) {
        return GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch(orionApiPrefix + "/" + path + "/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation
                        .addParametersItem(buildSecurityHeaderParameter()))
                .build();
    }

    /**
     * @return Authorization 认证请求头参数
     */
    private Parameter buildSecurityHeaderParameter() {
        return new Parameter()
                .name(HttpHeaders.AUTHORIZATION)
                .description("认证 Token")
                .in(String.valueOf(SecurityScheme.In.HEADER))
                .schema(new StringSchema()
                        ._default("Bearer 1")
                        .name(HttpHeaders.AUTHORIZATION)
                        .description("认证 Token"));
    }

}
