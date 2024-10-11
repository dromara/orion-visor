/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.framework.test.configuration;

import com.github.fppt.jedismock.RedisServer;
import com.orion.visor.framework.common.lock.Locker;
import com.orion.visor.framework.common.utils.LockerUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.util.function.Supplier;

/**
 * 单元测试 redis mock server 初始化
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
     * @return redisMockServer
     */
    @Bean
    public RedisServer redisMockServer(RedisProperties properties) {
        RedisServer server = new RedisServer(properties.getPort());
        try {
            server.start();
        } catch (Exception ignore) {
        }
        return server;
    }

    /**
     * @return 单元测试分布式锁
     */
    @Bean
    public Locker unitTestLocker() {
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
