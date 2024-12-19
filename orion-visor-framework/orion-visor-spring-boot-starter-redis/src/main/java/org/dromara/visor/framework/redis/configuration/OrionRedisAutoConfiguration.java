/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.redis.configuration;

import org.dromara.visor.framework.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.common.lock.Locker;
import org.dromara.visor.framework.common.utils.LockerUtils;
import org.dromara.visor.framework.redis.configuration.config.RedissonConfig;
import org.dromara.visor.framework.redis.core.lock.RedisLocker;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
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

