package com.orion.ops.framework.monitor.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.utils.jackson.AdminServerModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 项目 admin console 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/11 14:13
 */
@EnableAdminServer
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_MONITOR)
public class OrionAdminAutoConfiguration {

    /**
     * @param converter jackson converter
     * @return springboot-admin 序列化配置
     */
    @Bean
    @ConditionalOnBean(MappingJackson2HttpMessageConverter.class)
    public SimpleModule registrationModuleConverter(MappingJackson2HttpMessageConverter converter) {
        ObjectMapper objectMapper = converter.getObjectMapper();
        // 序列化配置
        AdminServerModule module = new AdminServerModule(new String[]{".*password$"});
        objectMapper.registerModule(module);
        return module;
    }

}
