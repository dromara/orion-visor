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
package org.dromara.visor.framework.config.core.store;

import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;

/**
 * 可控配置中心
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 23:00
 */
public interface ManagementConfigStore extends ConfigStore {

    /**
     * 加载全部配置
     */
    void loadAllConfig();

    /**
     * 覆盖配置
     *
     * @param key   key
     * @param value value
     */
    void override(String key, String value);

    /**
     * 注册 ref
     *
     * @param ref ref
     */
    void register(ConfigRef<?> ref);

}
