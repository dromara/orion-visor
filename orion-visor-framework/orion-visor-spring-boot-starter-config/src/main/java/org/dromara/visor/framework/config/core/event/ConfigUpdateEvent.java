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
package org.dromara.visor.framework.config.core.event;

import cn.orionsec.kit.lang.utils.collect.Maps;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 配置更新事件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 17:24
 */
public class ConfigUpdateEvent extends ApplicationEvent {

    private ConfigUpdateEvent(Map<String, String> configs) {
        super(configs);
    }

    public static ConfigUpdateEvent of(Map<String, String> configs) {
        return new ConfigUpdateEvent(configs);
    }

    public static ConfigUpdateEvent of(String key, String value) {
        return new ConfigUpdateEvent(Maps.of(key, value));
    }

}
