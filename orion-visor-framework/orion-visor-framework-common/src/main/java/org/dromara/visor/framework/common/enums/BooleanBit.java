/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * boolean 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 10:32
 */
@Getter
@AllArgsConstructor
public enum BooleanBit {

    /**
     * 假
     */
    FALSE(0),

    /**
     * 真
     */
    TRUE(1),

    ;

    private final Integer value;

    /**
     * 是否为布尔值
     *
     * @return boolean
     */
    public boolean booleanValue() {
        return this == TRUE;
    }

    public static BooleanBit of(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static BooleanBit of(Integer value) {
        if (value == null) {
            return null;
        }
        for (BooleanBit e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 转为布尔值
     *
     * @param value value
     * @return boolean
     */
    public static boolean toBoolean(Integer value) {
        return TRUE.value.equals(value);
    }

}
