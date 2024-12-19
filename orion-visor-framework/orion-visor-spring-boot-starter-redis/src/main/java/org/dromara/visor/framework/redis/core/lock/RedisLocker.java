/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.utils.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.lock.Locker;
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
