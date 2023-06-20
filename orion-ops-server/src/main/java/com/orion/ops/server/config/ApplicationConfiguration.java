package com.orion.ops.server.config;

import com.orion.spring.SpringHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:34
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * @return spring 容器工具类
     */
    @Bean
    public SpringHolder.ApplicationContextAwareStore springHolderAware() {
        return new SpringHolder.ApplicationContextAwareStore();
    }

}
