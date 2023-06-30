package com.orion.ops.framework.desensitize.config;

import com.orion.ops.framework.desensitize.core.handler.DesensitizeResultHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 数据脱敏置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:55
 */
@AutoConfiguration
public class OrionDesensitizeAutoConfiguration {

    /**
     * @return 返回结果脱敏处理器
     */
    @Bean
    public DesensitizeResultHandler desensitizeResultHandler() {
        return new DesensitizeResultHandler();
    }

}
