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
package org.dromara.visor.framework.executor.configuration.config;

import cn.orionsec.kit.lang.define.thread.RejectPolicy;
import cn.orionsec.kit.lang.utils.Systems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 线程池配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/16 10:51
 */
@Data
@Builder
@AllArgsConstructor
public class ExecutorConfig {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 是否使用 MDC
     */
    private boolean mdc;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maxPoolSize;

    /**
     * 队列容量
     */
    private int queueCapacity;

    /**
     * 空闲线程存活时间
     */
    private int keepAliveSeconds;

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreTimeout;

    /**
     * 是否使用同步队列
     */
    private boolean synchronousQueue;

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix;

    /**
     * 拒绝策略
     */
    private RejectPolicy rejectPolicy;

    public ExecutorConfig() {
        this.enabled = true;
        this.corePoolSize = Systems.PROCESS_NUM;
        this.maxPoolSize = Systems.PROCESS_NUM;
        this.queueCapacity = 100;
        this.keepAliveSeconds = 300;
        this.rejectPolicy = RejectPolicy.CALLER_RUNS;
    }

}
