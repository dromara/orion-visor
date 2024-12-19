/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
 * 主机系统类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/16 21:58
 */
@Getter
@AllArgsConstructor
public enum HostOsTypeEnum {

    /**
     * linux
     */
    LINUX(".sh"),

    /**
     * windows
     */
    WINDOWS(".cmd"),

    ;

    private final String scriptSuffix;

    public static HostOsTypeEnum of(String type) {
        if (type == null) {
            return LINUX;
        }
        for (HostOsTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return LINUX;
    }

    /**
     * 是否为 linux 系统
     *
     * @param type type
     * @return isLinux
     */
    public static boolean isLinux(String type) {
        return LINUX.name().equals(type);
    }

    /**
     * 是否为 windows 系统
     *
     * @param type type
     * @return isWindows
     */
    public static boolean isWindows(String type) {
        return WINDOWS.name().equals(type);
    }

}
