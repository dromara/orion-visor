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
package org.dromara.visor.module.asset.define.config;

import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;
import org.dromara.visor.common.constant.ConfigKeys;
import org.springframework.stereotype.Component;

/**
 * 自动清理设置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/24 15:01
 */
@Component
public class AppAutoClearConfig {

    /**
     * 是否开启自动清理命令记录
     */
    private final ConfigRef<Boolean> execLogEnabled;

    /**
     * 自动清理命令记录保留天数
     */
    private final ConfigRef<Integer> execLogKeepDays;

    /**
     * 是否开启自动清理终端连接记录
     */
    private final ConfigRef<Boolean> terminalLogEnabled;

    /**
     * 自动清理终端连接记录保留天数
     */
    private final ConfigRef<Integer> terminalLogKeepDays;

    public AppAutoClearConfig(ConfigStore configStore) {
        this.execLogEnabled = configStore.bool(ConfigKeys.AUTO_CLEAR_EXEC_LOG_ENABLED);
        this.execLogKeepDays = configStore.int32(ConfigKeys.AUTO_CLEAR_EXEC_LOG_KEEP_DAYS);
        this.terminalLogEnabled = configStore.bool(ConfigKeys.AUTO_CLEAR_TERMINAL_LOG_ENABLED);
        this.terminalLogKeepDays = configStore.int32(ConfigKeys.AUTO_CLEAR_TERMINAL_LOG_KEEP_DAYS);
    }

    public Boolean getExecLogEnabled() {
        return execLogEnabled.value;
    }

    public Integer getExecLogKeepDays() {
        return execLogKeepDays.value;
    }

    public Boolean getTerminalLogEnabled() {
        return terminalLogEnabled.value;
    }

    public Integer getTerminalLogKeepDays() {
        return terminalLogKeepDays.value;
    }

}
