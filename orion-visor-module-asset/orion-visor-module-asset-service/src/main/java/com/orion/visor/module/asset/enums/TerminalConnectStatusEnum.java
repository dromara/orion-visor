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
package com.orion.visor.module.asset.enums;

import com.orion.lang.utils.collect.Lists;

import java.util.List;

/**
 * 终端连接状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 22:27
 */
public enum TerminalConnectStatusEnum {

    /**
     * 连接中
     */
    CONNECTING,

    /**
     * 完成
     */
    COMPLETE,

    /**
     * 失败
     */
    FAILED,

    /**
     * 强制下线
     */
    FORCE_OFFLINE,

    ;

    public static final List<String> FINISH_STATUS_LIST = Lists.of(COMPLETE.name(), FAILED.name(), FORCE_OFFLINE.name());

    public static TerminalConnectStatusEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (TerminalConnectStatusEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
