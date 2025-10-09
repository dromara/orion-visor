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
package org.dromara.visor.module.common.config;

import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;
import org.dromara.visor.common.constant.ConfigKeys;
import org.springframework.stereotype.Component;

/**
 * 应用日志设置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 22:00
 */
@Component
public class AppLogConfig {

    /**
     * 日志加载偏移行
     */
    private final ConfigRef<Integer> trackerLoadLines;

    /**
     * 日志加载间隔毫秒
     */
    private final ConfigRef<Integer> trackerLoadInterval;

    /**
     * 是否生成详细的执行日志
     */
    private final ConfigRef<Boolean> execDetailEnabled;

    public AppLogConfig(ConfigStore configStore) {
        this.trackerLoadLines = configStore.int32(ConfigKeys.LOG_TRACKER_LOAD_LINES);
        this.trackerLoadInterval = configStore.int32(ConfigKeys.LOG_TRACKER_LOAD_INTERVAL);
        this.execDetailEnabled = configStore.bool(ConfigKeys.LOG_EXEC_DETAIL_ENABLED);
    }

    public Integer getTrackerLoadLines() {
        return trackerLoadLines.value;
    }

    public Integer getTrackerLoadInterval() {
        return trackerLoadInterval.value;
    }

    public Boolean getExecDetailEnabled() {
        return execDetailEnabled.value;
    }

}
