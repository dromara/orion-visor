package com.orion.ops.framework.desensitize.config;

import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import com.orion.ops.framework.desensitize.core.filter.DesensitizeValueFilter;
import com.orion.ops.framework.desensitize.core.serializer.DesensitizeJsonSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

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
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_DESENSITIZE)
public class OrionDesensitizeAutoConfiguration {

    /**
     * @return fastjson 序列化脱敏过滤器
     */
    @Bean
    public DesensitizeValueFilter desensitizeValueFilter() {
        return new DesensitizeValueFilter();
    }

    /**
     * @return jackson 序列化脱敏过滤器
     */
    @Bean
    @ConditionalOnBean(MappingJackson2HttpMessageConverter.class)
    public DesensitizeJsonSerializer desensitizeJsonSerializer(MappingJackson2HttpMessageConverter converter) {
        DesensitizeJsonSerializer serializer = new DesensitizeJsonSerializer();
        return serializer;
    }

}
