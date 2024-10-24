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

/**
 * 主机认证类型 - ssh
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 19:01
 */
public enum HostSshAuthTypeEnum {

    /**
     * 密码认证
     */
    PASSWORD,

    /**
     * 密钥认证
     */
    KEY,

    /**
     * 身份认证
     */
    IDENTITY,

    ;

    public static HostSshAuthTypeEnum of(String type) {
        if (type == null) {
            return PASSWORD;
        }
        for (HostSshAuthTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return PASSWORD;
    }

}
