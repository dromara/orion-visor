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
package org.dromara.visor.framework.storage.configuration;

import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.common.interfaces.FileClient;
import org.dromara.visor.framework.storage.configuration.config.LocalStorageConfig;
import org.dromara.visor.framework.storage.configuration.config.LogsStorageConfig;
import org.dromara.visor.framework.storage.core.client.local.LocalFileClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 存储配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 16:21
 * <p>
 * TODO 后续添加 FAST MINIO OSS 等
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_STORAGE)
@EnableConfigurationProperties({LocalStorageConfig.class, LogsStorageConfig.class})
public class OrionStorageAutoConfiguration {

    /**
     * @return 本地文件客户端
     */
    @Bean
    @ConditionalOnProperty(value = "orion.storage.local.enabled", havingValue = "true")
    public FileClient localFileClient(LocalStorageConfig config) {
        return new LocalFileClient(config);
    }

    /**
     * @return 日志文件客户端
     */
    @Bean
    @ConditionalOnProperty(value = "orion.storage.logs.enabled", havingValue = "true")
    public FileClient logsFileClient(LogsStorageConfig config) {
        return new LocalFileClient(config);
    }

}
