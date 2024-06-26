package com.orion.visor.framework.redis.configuration;

import com.orion.visor.framework.common.constant.AutoConfigureOrderConst;
import com.orion.visor.framework.common.lock.Locker;
import com.orion.visor.framework.common.utils.LockerUtils;
import com.orion.visor.framework.redis.configuration.config.RedissonConfig;
import com.orion.visor.framework.redis.core.lock.RedisLocker;
import com.orion.visor.framework.redis.core.utils.RedisUtils;
import org.redisson.api.RedissonClient;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/28 14:44
 */
@Lazy(false)
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_REDIS)
@EnableConfigurationProperties(RedissonConfig.class)
public class OrionRedisAutoConfiguration {

    /**
     * @param redisConnectionFactory factory
     * @return RedisTemplate
     */
    @Primary
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.string());
        redisTemplate.afterPropertiesSet();
        // 将其设置到 RedisUtils
        RedisUtils.setRedisTemplate(redisTemplate);
        return redisTemplate;
    }

    /**
     * @param redissonConfig config
     * @return redisson 自定义配置
     */
    @Bean
    public RedissonAutoConfigurationCustomizer redissonConfigurationCustomizer(RedissonConfig redissonConfig) {
        return config -> {
            config.setThreads(redissonConfig.getThreads());
            config.setNettyThreads(redissonConfig.getNettyThreads());
            // 单机配置
            SingleServerConfig single = config.useSingleServer();
            single.setConnectionMinimumIdleSize(redissonConfig.getMinimumIdleSize());
        };
    }

    /**
     * @param redissonClient redissonClient
     * @return redis 分布式锁
     */
    @Bean
    @ConditionalOnMissingBean
    public Locker redisLocker(RedissonClient redissonClient) {
        RedisLocker redisLocker = new RedisLocker(redissonClient);
        LockerUtils.setDelegate(redisLocker);
        return redisLocker;
    }

}

