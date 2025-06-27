/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import com.github.fppt.jedismock.RedisServer;
import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.common.interfaces.Locker;
import org.dromara.visor.common.utils.LockerUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.net.InetAddress;
import java.util.function.Supplier;

/**
 * noRedis 配置
 * 仅用于本地调试无 redis 的情况
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/26 10:02
 */
@ConditionalOnProperty(value = "no.redis", havingValue = "true")
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_REDIS - 10)
public class OrionNoRedisAutoConfiguration {

    /**
     * @return mocked redis server
     */
    @Bean
    public RedisServer redisServer(RedisProperties properties) {
        RedisServer server = new RedisServer(properties.getPort(), InetAddress.getLoopbackAddress());
        try {
            server.start();
        } catch (Exception ignore) {
        }
        return server;
    }

    /**
     * @return mocked jedis factory
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisServer redisServer) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisServer.getHost());
        config.setPort(redisServer.getBindPort());
        return new JedisConnectionFactory(config);
    }

    /**
     * @return mocked redis locker
     */
    @Bean
    public Locker redisLocker() {
        Locker locker = new Locker() {
            @Override
            public boolean tryLock(String key, Runnable run) {
                run.run();
                return true;
            }

            @Override
            public <T> T tryLock(String key, Supplier<T> call) {
                return call.get();
            }
        };
        LockerUtils.setDelegate(locker);
        return locker;
    }

}
