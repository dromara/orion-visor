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
