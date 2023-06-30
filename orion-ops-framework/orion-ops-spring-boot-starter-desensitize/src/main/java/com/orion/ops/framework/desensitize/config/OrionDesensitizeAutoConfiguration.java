package com.orion.ops.framework.desensitize.config;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.orion.lang.utils.Arrays1;
import com.orion.ops.framework.desensitize.core.filter.DesensitizeValueSerializeFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * 数据脱敏置类
 * <p>
 * 前置需要装配 orion-ops-spring-boot-starter-web
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:55
 */
@AutoConfiguration
@AutoConfigureAfter(name = "com.orion.ops.framework.web.config.OrionWebAutoConfiguration")
public class OrionDesensitizeAutoConfiguration {

    /**
     * @return 返回 序列化脱敏过滤器
     */
    @Bean
    @ConditionalOnBean(FastJsonHttpMessageConverter.class)
    public DesensitizeValueSerializeFilter desensitizeResultHandler(FastJsonHttpMessageConverter converter) {
        DesensitizeValueSerializeFilter desensitizeFilter = new DesensitizeValueSerializeFilter();
        // 获取 json 配置
        FastJsonConfig config = converter.getFastJsonConfig();
        SerializeFilter[] filters = config.getSerializeFilters();
        int filterLength = Arrays1.length(filters);
        if (filterLength == 0) {
            // 未设置配置
            filters = new SerializeFilter[]{desensitizeFilter};
        } else {
            SerializeFilter[] newFilters = new SerializeFilter[filterLength + 1];
            System.arraycopy(filters, 0, newFilters, 0, filterLength);
            newFilters[filterLength] = desensitizeFilter;
            filters = newFilters;
        }
        // 更新到配置
        config.setSerializeFilters(filters);
        return desensitizeFilter;
    }

}
