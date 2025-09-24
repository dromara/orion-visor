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
package org.dromara.visor.framework.influxdb.configuration;

import cn.orionsec.kit.lang.utils.Strings;
import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.influxdb.configuration.config.InfluxdbConfig;
import org.dromara.visor.framework.influxdb.core.utils.InfluxdbUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.net.ConnectException;

/**
 * influxdb 配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/10 20:35
 */
@Lazy(false)
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_INFLUXDB)
@ConditionalOnProperty(value = "spring.influxdb.enabled", havingValue = "true")
@EnableConfigurationProperties(InfluxdbConfig.class)
public class OrionInfluxdbAutoConfiguration {

    /**
     * @param config config
     * @return influxdb 客户端
     */
    @Bean(name = "influxDBClient")
    public InfluxDBClient influxDBClient(InfluxdbConfig config) throws ConnectException {
        // 参数
        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(config.getUrl())
                .authenticateToken(config.getToken().toCharArray())
                .org(config.getOrg())
                .bucket(config.getBucket())
                .logLevel(LogLevel.NONE)
                .build();
        // 客户端
        InfluxDBClient client = InfluxDBClientFactory.create(options);
        // 尝试连接
        Boolean ping = client.ping();
        if (!ping) {
            throw new ConnectException(Strings.format("connect to influxdb failed. url: {}, org: {}", config.getUrl(), config.getOrg()));
        }
        // 设置工具类
        InfluxdbUtils.setInfluxClient(config.getBucket(), client);
        return client;
    }

}
