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
package org.dromara.visor.framework.influxdb.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * influxdb 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/10 20:36
 */
@Data
@ConfigurationProperties("spring.influxdb")
public class InfluxdbConfig {

    /**
     * url
     */
    private String url;

    /**
     * org
     */
    private String org;

    /**
     * bucket
     */
    private String bucket;

    /**
     * apiToken
     */
    private String token;

}
