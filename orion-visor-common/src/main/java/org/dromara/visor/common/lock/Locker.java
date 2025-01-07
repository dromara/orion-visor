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
