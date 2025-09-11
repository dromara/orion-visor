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
package org.dromara.visor.common.lock;

import cn.orionsec.kit.lang.able.Executable;

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
     * 尝试获取锁并执行
     *
     * @param key        key
     * @param executable exec
     * @return 是否获取到锁
     */
    boolean tryLockExecute(String key, Executable executable);

    /**
     * 尝试获取锁并执行
     *
     * @param key        key
     * @param timeout    timeout
     * @param executable exec
     * @return 是否获取到锁
     */
    boolean tryLockExecute(String key, long timeout, Executable executable);

    /**
     * 尝试获取锁并执行 未获取到锁则抛出异常
     *
     * @param key      key
     * @param callable callable
     * @param <T>      T
     * @return 执行结果
     */
    <T> T tryLockExecute(String key, Supplier<T> callable);

    /**
     * 尝试获取锁并执行 未获取到锁则抛出异常
     *
     * @param key      key
     * @param timeout  timeout
     * @param callable callable
     * @param <T>      T
     * @return 执行结果
     */
    <T> T tryLockExecute(String key, long timeout, Supplier<T> callable);

    /**
     * 阻塞获取锁并执行
     *
     * @param key        key
     * @param executable exec
     */
    void lockExecute(String key, Executable executable);

    /**
     * 阻塞获取锁并执行
     *
     * @param key        key
     * @param timeout    timeout
     * @param executable exec
     */
    void lockExecute(String key, long timeout, Executable executable);

    /**
     * 阻塞获取锁并执行
     *
     * @param key      key
     * @param callable callable
     * @param <T>      T
     * @return 执行结果
     */
    <T> T lockExecute(String key, Supplier<T> callable);

    /**
     * 阻塞获取锁并执行
     *
     * @param key      key
     * @param timeout  timeout
     * @param callable callable
     * @param <T>      T
     * @return 执行结果
     */
    <T> T lockExecute(String key, long timeout, Supplier<T> callable);

}
