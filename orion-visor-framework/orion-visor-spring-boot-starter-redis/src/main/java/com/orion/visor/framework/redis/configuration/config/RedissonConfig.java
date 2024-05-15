package com.orion.visor.framework.redis.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/27 15:17
 */
@Data
@ConfigurationProperties("spring.redisson")
public class RedissonConfig {

    /**
     * 任务线程数
     */
    private Integer threads;

    /**
     * netty 线程数
     */
    private Integer nettyThreads;

}
