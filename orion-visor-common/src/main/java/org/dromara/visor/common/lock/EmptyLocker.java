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
 * 空实现的锁
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/23 13:59
 */
public class EmptyLocker implements Locker {

    @Override
    public boolean tryLockExecute(String key, Executable executable) {
        executable.exec();
        return true;
    }

    @Override
    public boolean tryLockExecute(String key, long timeout, Executable executable) {
        executable.exec();
        return true;
    }

    @Override
    public <T> T tryLockExecute(String key, Supplier<T> callable) {
        return callable.get();
    }

    @Override
    public <T> T tryLockExecute(String key, long timeout, Supplier<T> callable) {
        return callable.get();
    }

    @Override
    public void lockExecute(String key, Executable executable) {
        executable.exec();
    }

    @Override
    public void lockExecute(String key, long timeout, Executable executable) {
        executable.exec();
    }

    @Override
    public <T> T lockExecute(String key, Supplier<T> callable) {
        return callable.get();
    }

    @Override
    public <T> T lockExecute(String key, long timeout, Supplier<T> callable) {
        return callable.get();
    }

}
