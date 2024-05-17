package com.orion.visor.framework.common.utils;

import com.orion.visor.framework.common.meta.TraceIdHolder;
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
