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
package org.dromara.visor.framework.job.configuration;

import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.thread.ThreadPoolMdcTaskExecutor;
import org.dromara.visor.framework.job.configuration.config.AsyncExecutorConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * async 异步任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/20 10:34
 */
@EnableAsync
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_JOB_ASYNC)
@EnableConfigurationProperties(AsyncExecutorConfig.class)
public class OrionAsyncAutoConfiguration {

    /**
     * {@code @Async("asyncExecutor")}
     *
     * @return 支持 MDC 的异步线程池
     */
    @Bean(name = "asyncExecutor")
    public TaskExecutor asyncExecutor(AsyncExecutorConfig config) {
        ThreadPoolMdcTaskExecutor executor = new ThreadPoolMdcTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(true);
        executor.setThreadNamePrefix("async-task-");
        // 设置等待所有任务执行结束再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 以确保应用最后能够被关闭
        executor.setAwaitTerminationSeconds(60);
        // 调用者调用拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * {@code @Async("metricsExecutor")}
     *
     * @return 指标线程池
     */
    @Bean(name = "metricsExecutor")
    public TaskExecutor metricsExecutor() {
        ThreadPoolMdcTaskExecutor executor = new ThreadPoolMdcTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(Const.MS_S_60);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setThreadNamePrefix("metrics-task-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
