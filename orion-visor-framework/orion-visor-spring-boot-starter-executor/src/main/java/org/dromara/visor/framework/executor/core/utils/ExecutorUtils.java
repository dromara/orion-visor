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
package org.dromara.visor.framework.executor.core.utils;

import cn.orionsec.kit.lang.able.Executable;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.collect.Maps;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * 线程池工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/16 16:34
 */
public class ExecutorUtils {

    private static Map<String, ThreadPoolTaskExecutor> executorMap;

    private ExecutorUtils() {
    }

    /**
     * 获取线程池
     *
     * @param name name
     * @return executor
     */
    public static ThreadPoolTaskExecutor getExecutor(String name) {
        return executorMap.get(name);
    }

    /**
     * 执行
     *
     * @param name     name
     * @param runnable runnable
     */
    public static void execute(String name, Runnable runnable) {
        getExecutor(name).execute(runnable);
    }

    /**
     * 执行
     *
     * @param name       name
     * @param executable executable
     */
    public static void execute(String name, Executable executable) {
        getExecutor(name).execute(executable::exec);
    }

    public static void setExecutors(Map<String, ThreadPoolTaskExecutor> executorMap) {
        if (ExecutorUtils.executorMap != null) {
            // unmodified
            throw Exceptions.state();
        }
        ExecutorUtils.executorMap = Maps.unmodified(executorMap);
    }

}
