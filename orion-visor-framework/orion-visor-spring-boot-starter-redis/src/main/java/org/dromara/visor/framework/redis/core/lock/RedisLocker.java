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
package org.dromara.visor.framework.redis.core.lock;

import cn.orionsec.kit.lang.able.Executable;
import cn.orionsec.kit.lang.utils.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.lock.Locker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
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

    private static final String LOCK_KEY_PREFIX = "lock:";

    private final RedissonClient redissonClient;

    public RedisLocker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLockExecute(String key, Executable executable) {
        return this.tryLockExecute(key, 0, executable);
    }

    @Override
    public boolean tryLockExecute(String key, long timeout, Executable executable) {
        // 获取锁
        RLock lock = this.getLock(key);
        // 未获取到直接返回
        if (this.tryLock(lock, timeout)) {
            return false;
        }
        // 执行
        try {
            executable.exec();
        } finally {
            this.unlockSafe(lock);
        }
        return true;
    }

    @Override
    public <T> T tryLockExecute(String key, Supplier<T> callable) {
        return this.tryLockExecute(key, 0, callable);
    }

    @Override
    public <T> T tryLockExecute(String key, long timeout, Supplier<T> callable) {
        // 获取锁
        RLock lock = this.getLock(key);
        // 未获取到直接返回
        if (this.tryLock(lock, timeout)) {
            throw Exceptions.lock();
        }
        // 执行
        try {
            return callable.get();
        } finally {
            this.unlockSafe(lock);
        }
    }

    @Override
    public void lockExecute(String key, Executable executable) {
        this.lockExecute(key, 0, executable);
    }

    @Override
    public void lockExecute(String key, long timeout, Executable executable) {
        // 获取锁
        RLock lock = this.getLock(key);
        this.lock(lock, timeout);
        // 执行
        try {
            executable.exec();
        } finally {
            this.unlockSafe(lock);
        }
    }

    @Override
    public <T> T lockExecute(String key, Supplier<T> callable) {
        return this.lockExecute(key, 0, callable);
    }

    @Override
    public <T> T lockExecute(String key, long timeout, Supplier<T> callable) {
        // 获取锁
        RLock lock = this.getLock(key);
        this.lock(lock, timeout);
        // 执行
        try {
            return callable.get();
        } finally {
            this.unlockSafe(lock);
        }
    }

    /**
     * 获取锁
     *
     * @param key key
     * @return lock
     */
    private RLock getLock(String key) {
        return redissonClient.getLock(LOCK_KEY_PREFIX + key);
    }

    /**
     * 尝试上锁
     *
     * @param lock    lock
     * @param timeout timeout
     * @return locked
     */
    private boolean tryLock(RLock lock, long timeout) {
        boolean result;
        try {
            if (timeout == 0) {
                result = lock.tryLock();
            } else {
                result = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
            }
            if (!result) {
                log.warn("RedisLocker.tryLock failed {}", lock.getName());
            }
        } catch (InterruptedException e) {
            log.error("RedisLocker.tryLock timed out {}", lock.getName(), e);
            throw Exceptions.lock(e);
        } catch (Exception e) {
            log.error("RedisLocker.tryLock error {}", lock.getName(), e);
            throw Exceptions.lock(e);
        }
        return result;
    }

    /**
     * 上锁
     *
     * @param lock    lock
     * @param timeout timeout
     */
    private void lock(RLock lock, long timeout) {
        try {
            if (timeout == 0) {
                lock.lock();
            } else {
                lock.lock(timeout, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("RedisLocker.lock lock error {}", lock.getName(), e);
            throw Exceptions.lock(e);
        }
    }

    /**
     * 安全的释放锁
     *
     * @param lock lock
     */
    private void unlockSafe(RLock lock) {
        if (!lock.isHeldByCurrentThread()) {
            return;
        }
        try {
            lock.unlock();
        } catch (Exception e) {
            log.warn("RedisLocker.unlock failed {}", lock.getName(), e);
        }
    }

}
