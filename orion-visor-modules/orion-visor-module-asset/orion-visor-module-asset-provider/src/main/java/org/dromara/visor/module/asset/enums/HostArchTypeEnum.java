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
 * 主机系统架构类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/16 21:58
 */
@Getter
@AllArgsConstructor
public enum HostArchTypeEnum {

    /**
     * X86_64
     */
    AMD64,

    /**
     * arm64
     */
    ARM64,

    ;

    public boolean is(String type) {
        if (type == null) {
            return false;
        }
        return type.equalsIgnoreCase(this.name());
    }

    public static HostArchTypeEnum of(String type) {
        if (type == null) {
            return AMD64;
        }
        type = type.toUpperCase();
        for (HostArchTypeEnum value : values()) {
            if (value.name().equals(type) || type.contains(value.name())) {
                return value;
            }
        }
        return AMD64;
    }

}
