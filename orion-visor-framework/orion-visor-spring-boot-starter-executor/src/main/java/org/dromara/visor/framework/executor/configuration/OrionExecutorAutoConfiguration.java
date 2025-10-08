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
package org.dromara.visor.framework.executor.configuration;

import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.executor.configuration.config.ExecutorsConfig;
import org.dromara.visor.framework.executor.core.context.ExecutorContext;
import org.dromara.visor.framework.executor.core.utils.ExecutorUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * 项目线程池配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/16 14:34
 */
@EnableAsync
@AutoConfiguration
@EnableConfigurationProperties({ExecutorsConfig.class})
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_EXECUTOR)
public class OrionExecutorAutoConfiguration {

    /**
     * 创建线程池上下文
     *
     * @param executorsConfig executorsConfig
     * @param beanFactory     beanFactory
     * @return 线程池上下文
     */
    @Bean
    public ExecutorContext executorContext(ExecutorsConfig executorsConfig,
                                           ConfigurableBeanFactory beanFactory) {
        // 创建上下文
        ExecutorContext context = new ExecutorContext(executorsConfig, beanFactory);
        Map<String, ThreadPoolTaskExecutor> executorMap = context.init();
        // 获取线程池列表
        ExecutorUtils.setExecutors(executorMap);
        return context;
    }

}
