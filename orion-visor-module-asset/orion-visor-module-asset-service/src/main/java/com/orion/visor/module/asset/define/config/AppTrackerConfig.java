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
package com.orion.visor.module.asset.define.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用 tracker 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 22:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.tracker")
public class AppTrackerConfig {

    /**
     * 加载偏移量 (行)
     */
    private Integer offset;

    /**
     * 延迟时间 (ms)
     */
    private Integer delay;

    /**
     * 文件未找到等待次数
     */
    private Integer waitTimes;

    public AppTrackerConfig() {
        this.offset = 300;
        this.delay = 100;
        this.waitTimes = 100;
    }
}
