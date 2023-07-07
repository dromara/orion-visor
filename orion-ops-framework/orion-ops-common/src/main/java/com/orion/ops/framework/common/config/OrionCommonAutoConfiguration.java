package com.orion.ops.framework.common.config;

import com.orion.spring.SpringHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 应用配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:34
 */
@AutoConfiguration
public class OrionCommonAutoConfiguration {

    /**
     * @return spring 容器工具类
     */
    @Bean
    public SpringHolder.ApplicationContextAwareStore springHolderAware() {
        return new SpringHolder.ApplicationContextAwareStore();
    }

}
