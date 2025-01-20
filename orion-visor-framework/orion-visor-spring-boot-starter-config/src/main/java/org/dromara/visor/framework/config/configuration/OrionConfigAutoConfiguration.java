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
package org.dromara.visor.framework.config.configuration;

import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.config.core.listener.ConfigUpdateListener;
import org.dromara.visor.framework.config.core.service.ConfigFrameworkService;
import org.dromara.visor.framework.config.core.service.ConfigFrameworkServiceDelegate;
import org.dromara.visor.framework.config.core.store.ManagementConfigStore;
import org.dromara.visor.framework.config.core.store.ManagementConfigStoreImpl;
import org.dromara.visor.framework.config.core.utils.ConfigStores;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 配置中心 自动配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 15:44
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_CONFIG)
public class OrionConfigAutoConfiguration {

    /**
     * @param impl impl
     * @return 配置框架服务
     */
    @Bean
    @Primary
    public ConfigFrameworkServiceDelegate configFrameworkService(ConfigFrameworkService impl) {
        return new ConfigFrameworkServiceDelegate(impl);
    }

    /**
     * @param delegate delegate
     * @return 配置修改监听器
     */
    @Bean
    public ManagementConfigStore configStore(ConfigFrameworkServiceDelegate delegate) {
        // 初始化
        ManagementConfigStoreImpl configStore = new ManagementConfigStoreImpl(delegate);
        // 加载全部配置
        configStore.loadAllConfig();
        // 设置 ConfigStores
        ConfigStores.setDelegate(configStore);
        return configStore;
    }

    /**
     * @param configStore configStore
     * @return 配置更新监听器
     */
    @Bean
    public ConfigUpdateListener configUpdateListener(ManagementConfigStore configStore) {
        return new ConfigUpdateListener(configStore);
    }

}
