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
 * 告警触发条件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/14 14:02
 */
@Getter
@AllArgsConstructor
public enum AlarmTriggerConditionEnum {

    /**
     * 大于
     */
    GT(">"),

    /**
     * 大于等于
     */
    GE(">="),

    /**
     * 等于
     */
    EQ("=="),

    /**
     * 小于等于
     */
    LE("<="),

    /**
     * 小于
     */
    LT("<"),

    ;

    private final String condition;

    public static AlarmTriggerConditionEnum of(String name) {
        if (name == null) {
            return null;
        }
        for (AlarmTriggerConditionEnum e : AlarmTriggerConditionEnum.values()) {
            if (e.name().equals(name))
                return e;
        }
        return null;
    }

}
