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
package org.dromara.visor.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 脚本执行枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/17 10:17
 */
@Getter
@AllArgsConstructor
public enum ScriptExecEnum {

    /**
     * 不使用
     */
    DISABLED(0),

    /**
     * 使用
     */
    ENABLED(1),

    ;

    private final Integer value;

    public static ScriptExecEnum of(Integer value) {
        if (value == null) {
            return null;
        }
        for (ScriptExecEnum val : values()) {
            if (val.value.equals(value)) {
                return val;
            }
        }
        return null;

    }

    /**
     * 检查是否启用
     *
     * @param value value
     * @return 是否启用
     */
    public static boolean isEnabled(Integer value) {
        return ENABLED.value.equals(value);
    }

}
