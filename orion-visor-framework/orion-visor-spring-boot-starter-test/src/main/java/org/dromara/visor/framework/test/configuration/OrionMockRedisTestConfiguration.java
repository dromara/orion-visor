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
package org.dromara.visor.framework.test.configuration;

import com.github.fppt.jedismock.RedisServer;
import org.dromara.visor.common.lock.EmptyLocker;
import org.dromara.visor.common.lock.Locker;
import org.dromara.visor.common.utils.LockerUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.net.InetAddress;

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
        RedisServer server = new RedisServer(properties.getPort(), InetAddress.getLoopbackAddress());
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
        EmptyLocker locker = new EmptyLocker();
        LockerUtils.setDelegate(locker);
        return locker;
    }

}
