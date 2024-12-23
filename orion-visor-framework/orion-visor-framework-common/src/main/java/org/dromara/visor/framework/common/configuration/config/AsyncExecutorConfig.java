/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.framework.common.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 15:49
 */
@Data
@ConfigurationProperties(prefix = "orion.async.executor")
public class AsyncExecutorConfig {

    /**
     * 核心线程数量
     */
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    private int maxPoolSize;

    /**
     * 队列容量
     */
    private int queueCapacity;

    /**
     * 活跃时间
     */
    private int keepAliveSeconds;

    public AsyncExecutorConfig() {
        this.corePoolSize = 8;
        this.maxPoolSize = 16;
        this.queueCapacity = 200;
        this.keepAliveSeconds = 300;
    }

}
