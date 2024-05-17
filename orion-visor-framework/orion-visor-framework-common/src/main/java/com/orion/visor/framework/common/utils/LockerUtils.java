package com.orion.visor.framework.common.utils;

import com.orion.lang.utils.Exceptions;
import com.orion.visor.framework.common.lock.Locker;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 分布式锁工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/25 16:42
 */
@Slf4j
public class LockerUtils {

    private static Locker delegate;

    private LockerUtils() {
    }

    /**
     * 尝试获取锁
     *
     * @param key key
     * @param run run
     * @return 是否获取到锁
     */
    public static boolean tryLock(String key, Runnable run) {
        return delegate.tryLock(key, run);
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
        return delegate.tryLock(key, call);
    }

    public static void setDelegate(Locker delegate) {
        if (LockerUtils.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        LockerUtils.delegate = delegate;
    }

}
