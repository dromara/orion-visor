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
package org.dromara.visor.module.monitor.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警事件来源
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/10/13 22:03
 */
@Getter
@AllArgsConstructor
public enum AlarmEventSourceTypeEnum {

    /**
     * 主机告警
     */
    HOST,

    /**
     * 拨测告警
     */
    UPTIME,

    ;

    public static AlarmEventSourceTypeEnum of(String value) {
        if (value == null) {
            return null;
        }
        for (AlarmEventSourceTypeEnum item : values()) {
            if (item.name().equals(value)) {
                return item;
            }
        }
        return null;
    }

}