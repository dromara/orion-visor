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
 * 告警级别枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/21 14:39
 */
@Getter
@AllArgsConstructor
public enum AlarmLevelEnum {

    /**
     * P0 - 严重 / Critical
     */
    P0(0, "严重", "Critical", "red"),

    /**
     * P1 - 重要 / Major
     */
    P1(1, "重要", "Major", "purple"),

    /**
     * P2 - 次要 / Minor
     */
    P2(2, "次要", "Minor", "magenta"),

    /**
     * P3 - 警告 / Warning
     */
    P3(3, "警告", "Warning", "orange"),

    /**
     * P4 - 信息 / Info
     */
    P4(4, "信息", "Info", "gray"),

    ;

    private final Integer value;

    private final String label;

    private final String severity;

    private final String color;

    public static AlarmLevelEnum of(Integer value) {
        if (value == null) {
            return P0;
        }
        for (AlarmLevelEnum alarmLevelEnum : AlarmLevelEnum.values()) {
            if (alarmLevelEnum.getValue().equals(value)) {
                return alarmLevelEnum;
            }
        }
        return P0;
    }

}

