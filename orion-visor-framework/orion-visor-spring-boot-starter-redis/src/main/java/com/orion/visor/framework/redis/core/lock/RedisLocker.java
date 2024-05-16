package com.orion.visor.framework.redis.core.lock;

import com.orion.lang.utils.Exceptions;
import com.orion.visor.framework.common.lock.Locker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.function.Supplier;

/**
 * redis 分布式锁
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/25 16:42
 */
@Slf4j
public class RedisLocker implements Locker {

    private final RedissonClient redissonClient;

    public RedisLocker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLock(String key, Runnable run) {
        // 获取锁
        RLock lock = redissonClient.getLock(key);
        // 未获取到直接返回
        if (!lock.tryLock()) {
            log.info("RedisLocker.tryLock failed {}", key);
            return false;
        }
        // 执行
        try {
            run.run();
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public <T> T tryLock(String key, Supplier<T> call) {
        // 获取锁
        RLock lock = redissonClient.getLock(key);
        // 未获取到直接返回
        if (!lock.tryLock()) {
            log.info("RedisLocker.tryLock failed {}", key);
            throw Exceptions.lock();
        }
        // 执行
        try {
            return call.get();
        } finally {
            lock.unlock();
        }
    }

}
