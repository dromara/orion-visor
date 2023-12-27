package com.orion.ops.framework.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * swagger 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/21 11:13
 */
@Data
@ConfigurationProperties("orion.swagger")
public class SwaggerConfig {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 版本
     */
    private String version;

    /**
     * url
     */
    private String url;

    /**
     * email
     */
    private String email;

    /**
     * license
     */
    private String license;

    /**
     * license-url
     */
    private String licenseUrl;

    /**
     * api 分组
     */
    private Map<String, GroupedApiConfig> groupedApi;

    @Data
    public static class GroupedApiConfig {

        /**
         * 名称
         */
        private String group;

        /**
         * 路径
         */
        private String path;

    }

}
