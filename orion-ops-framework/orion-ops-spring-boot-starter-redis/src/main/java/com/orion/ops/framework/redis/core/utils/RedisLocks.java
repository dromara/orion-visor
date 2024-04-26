package com.orion.ops.framework.redis.core.utils;

import com.orion.lang.utils.Exceptions;
import com.orion.ops.framework.redis.core.lock.RedisLocker;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * redis 分布式锁工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/25 16:42
 */
@Slf4j
public class RedisLocks {

    private static RedisLocker redisLocker;

    private RedisLocks() {
    }

    /**
     * 尝试获取锁
     *
     * @param key key
     * @param run run
     * @return 是否获取到锁
     */
    public static boolean tryLock(String key, Runnable run) {
        return redisLocker.tryLock(key, run);
    }

    /**
     * 尝试获取锁
     *
     * @param key  key
     * @param call call
     * @param <T>  T
     * @return 执行结果
     */
    public static <T> T tryLock(String key, Supplier<T> call) {
        return redisLocker.tryLock(key, call);
    }

    public static void setRedisLocker(RedisLocker redisLocker) {
        if (RedisLocks.redisLocker != null) {
            // unmodified
            throw Exceptions.state();
        }
        RedisLocks.redisLocker = redisLocker;
    }

}
