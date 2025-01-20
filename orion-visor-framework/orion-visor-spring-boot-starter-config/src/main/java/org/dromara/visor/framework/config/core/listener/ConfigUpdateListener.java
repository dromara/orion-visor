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
package org.dromara.visor.framework.config.core.listener;

import org.dromara.visor.framework.config.core.event.ConfigUpdateEvent;
import org.dromara.visor.framework.config.core.store.ManagementConfigStore;
import org.springframework.context.ApplicationListener;

import java.util.Map;

/**
 * 配置修改监听器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 16:01
 */
public class ConfigUpdateListener implements ApplicationListener<ConfigUpdateEvent> {

    private final ManagementConfigStore configStore;

    public ConfigUpdateListener(ManagementConfigStore configStore) {
        this.configStore = configStore;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ConfigUpdateEvent event) {
        // 获取修改的配置并且覆盖
        Map<String, String> configs = (Map<String, String>) event.getSource();
        configs.forEach(configStore::override);
    }

}
