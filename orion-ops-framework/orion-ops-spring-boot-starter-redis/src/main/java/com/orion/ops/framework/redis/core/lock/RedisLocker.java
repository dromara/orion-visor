package com.orion.ops.framework.redis.core.lock;

import com.orion.lang.utils.Exceptions;
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
public class RedisLocker {

    private final RedissonClient redissonClient;

    public RedisLocker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 尝试获取锁
     *
     * @param key key
     * @param run run
     * @return 是否获取到锁
     */
    public boolean tryLock(String key, Runnable run) {
        // 获取锁
        RLock lock = redissonClient.getLock(key);
        // 未获取到直接返回
        if (!lock.tryLock()) {
            log.info("RedisLocks.tryLock failed {}", key);
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

    /**
     * 尝试获取锁
     *
     * @param key  key
     * @param call call
     * @param <T>  T
     * @return 执行结果
     */
    public <T> T tryLock(String key, Supplier<T> call) {
        // 获取锁
        RLock lock = redissonClient.getLock(key);
        // 未获取到直接返回
        if (!lock.tryLock()) {
            log.info("RedisLocks.tryLock failed {}", key);
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
