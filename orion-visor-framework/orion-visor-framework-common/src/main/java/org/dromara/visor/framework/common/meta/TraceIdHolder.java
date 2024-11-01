/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.framework.common.meta;

import cn.orionsec.kit.lang.id.UUIds;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.MDC;

/**
 * traceId 持有者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 17:35
 */
public class TraceIdHolder {

    public static final String TRACE_ID_HEADER = "trace-id";

    public static final String TRACE_ID_MDC = "tid";

    private TraceIdHolder() {
    }

    /**
     * 请求序列
     */
    private static final ThreadLocal<String> HOLDER = new TransmittableThreadLocal<>();

    /**
     * 获取 traceId
     *
     * @return traceId
     */
    public static String get() {
        return HOLDER.get();
    }

    /**
     * 设置 traceId
     */
    public static void set() {
        set(createTraceId());
    }

    /**
     * 设置 traceId
     *
     * @param traceId traceId
     */
    public static void set(String traceId) {
        // 设置应用上下文
        HOLDER.set(traceId);
        // 设置日志上下文
        setMdc(traceId);
    }

    /**
     * 删除 traceId
     */
    public static void remove() {
        // 移除应用上下文
        HOLDER.remove();
        // 移除日志上下文
        removeMdc();
    }

    /**
     * 从应用上下文 设置到日志上下文
     */
    public static void setMdc() {
        setMdc(HOLDER.get());
    }

    /**
     * 设置到日志上下文
     *
     * @param traceId traceId
     */
    public static void setMdc(String traceId) {
        MDC.put(TRACE_ID_MDC, traceId);
    }

    /**
     * 移除日志上下文
     */
    public static void removeMdc() {
        MDC.remove(TRACE_ID_MDC);
    }

    /**
     * 创建 traceId
     *
     * @return traceId
     */
    public static String createTraceId() {
        return UUIds.random32();
    }

}
