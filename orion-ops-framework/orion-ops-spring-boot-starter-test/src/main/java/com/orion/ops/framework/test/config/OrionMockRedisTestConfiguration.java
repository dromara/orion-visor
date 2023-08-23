package com.orion.ops.framework.test.config;

import com.github.fppt.jedismock.RedisServer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

/**
 * 单元测试 redis 初始化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/23 17:19
 */
@Lazy(false)
@Profile("unit-test")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisProperties.class)
public class OrionMockRedisTestConfiguration {

    /**
     * mockRedisServer
     */
    @Bean
    public RedisServer redisServer(RedisProperties properties) {
        // TODO 看看正常情况下会不会有
        RedisServer redisServer = new RedisServer(properties.getPort());
        try {
            redisServer.start();
        } catch (Exception ignore) {
        }
        return redisServer;
    }

}
