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
package org.dromara.visor.framework.common.utils;

import org.dromara.visor.framework.common.meta.TraceIdHolder;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 多线程下 MDC 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 14:55
 */
public class ThreadMdcUtils {

    private ThreadMdcUtils() {
    }

    /**
     * 设置 MDC traceId
     */
    public static void setTraceIdIfAbsent() {
        if (MDC.get(TraceIdHolder.TRACE_ID_MDC) == null) {
            MDC.put(TraceIdHolder.TRACE_ID_MDC, TraceIdHolder.get());
        }
    }

    /**
     * 设置线程 MDC 上下文
     *
     * @param callable callable
     * @param <T>      T
     * @return callable
     */
    public static <T> Callable<T> wrap(Callable<T> callable) {
        // 获取当前线程 MDC 上下文
        Map<String, String> callerContext = MDC.getCopyOfContextMap();
        return () -> {
            if (callerContext == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(callerContext);
            }
            // 设置 traceId
            setTraceIdIfAbsent();
            // 执行线程并且清理MDC
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    /**
     * 设置线程 MDC 上下文
     *
     * @param runnable runnable
     * @return callable
     */
    public static Runnable wrap(Runnable runnable) {
        // 获取当前线程 MDC 上下文
        Map<String, String> callerContext = MDC.getCopyOfContextMap();
        return () -> {
            //
            if (callerContext == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(callerContext);
            }
            // 设置 traceId
            setTraceIdIfAbsent();
            // 执行线程并且清理MDC
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
