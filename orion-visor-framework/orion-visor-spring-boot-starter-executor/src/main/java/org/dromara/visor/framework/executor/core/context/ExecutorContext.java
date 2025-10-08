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
package org.dromara.visor.framework.executor.core.context;

import cn.orionsec.kit.lang.define.thread.RejectPolicy;
import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.thread.ThreadPoolMdcTaskExecutor;
import org.dromara.visor.framework.executor.configuration.config.ExecutorConfig;
import org.dromara.visor.framework.executor.configuration.config.ExecutorsConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行器上下文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/16 16:28
 */
@Slf4j
public class ExecutorContext {

    private final ExecutorsConfig executorsConfig;

    private final ConfigurableBeanFactory beanFactory;

    public ExecutorContext(ExecutorsConfig executorsConfig, ConfigurableBeanFactory beanFactory) {
        this.executorsConfig = executorsConfig;
        this.beanFactory = beanFactory;
    }

    /**
     * 初始化
     *
     * @return executorMap
     */
    public Map<String, ThreadPoolTaskExecutor> init() {
        log.info("--------- Executors init start --------- ");
        Map<String, ThreadPoolTaskExecutor> executorMap = new HashMap<>();
        executorsConfig.getExecutors().forEach((beanName, config) -> {
            String executorName = Strings.leftPad(beanName, 30);
            // 是否启用
            if (!config.isEnabled()) {
                log.info("Executor [{}] is disabled.", executorName);
                return;
            }
            // 创建线程池
            ThreadPoolTaskExecutor executor = this.createTaskExecutor(config);
            // 注册到容器中
            beanFactory.registerSingleton(beanName, executor);
            executorMap.put(beanName, executor);
            log.info("Executor [{}] init success. {}", executorName, JSON.toJSONString(config));
        });
        log.info("--------- Executors init end --------- ");
        return executorMap;
    }

    /**
     * 创建线程池
     *
     * @param config config
     */
    private ThreadPoolTaskExecutor createTaskExecutor(ExecutorConfig config) {
        // 创建线程池
        ThreadPoolTaskExecutor executor;
        if (config.isMdc()) {
            executor = new ThreadPoolMdcTaskExecutor();
        } else {
            executor = new ThreadPoolTaskExecutor();
        }
        // 同步队列
        if (config.isSynchronousQueue()) {
            config.setCorePoolSize(0);
            config.setMaxPoolSize(Integer.MAX_VALUE);
            config.setQueueCapacity(0);
            config.setRejectPolicy(RejectPolicy.ABORT);
        }
        // 设置参数
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setAllowCoreThreadTimeOut(config.isAllowCoreTimeout());
        executor.setRejectedExecutionHandler(config.getRejectPolicy().getHandler());
        // 设置等待所有任务执行结束再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 初始化
        executor.initialize();
        return executor;
    }

}
