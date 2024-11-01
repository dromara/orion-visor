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
package org.dromara.visor.framework.redis.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/27 15:17
 */
@Data
@ConfigurationProperties("spring.redisson")
public class RedissonConfig {

    /**
     * 任务线程数
     */
    private Integer threads;

    /**
     * netty 线程数
     */
    private Integer nettyThreads;

    /**
     * 最小空闲连接数
     */
    private Integer minimumIdleSize;

    public RedissonConfig() {
        this.threads = 16;
        this.nettyThreads = 16;
        this.minimumIdleSize = 16;
    }

}
