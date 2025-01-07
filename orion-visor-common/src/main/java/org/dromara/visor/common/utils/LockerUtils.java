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
package org.dromara.visor.common.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.lock.Locker;

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
