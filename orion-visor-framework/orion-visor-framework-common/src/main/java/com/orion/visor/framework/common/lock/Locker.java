package com.orion.visor.framework.common.lock;

import java.util.function.Supplier;

/**
 * 分布式锁
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/16 12:24
 */
public interface Locker {

    /**
     * 尝试获取锁
     *
     * @param key key
     * @param run run
     * @return 是否获取到锁
     */
    boolean tryLock(String key, Runnable run);

    /**
     * 尝试获取锁
     *
     * @param key  key
     * @param call call
     * @param <T>  T
     * @return 执行结果
     */
    <T> T tryLock(String key, Supplier<T> call);

}
