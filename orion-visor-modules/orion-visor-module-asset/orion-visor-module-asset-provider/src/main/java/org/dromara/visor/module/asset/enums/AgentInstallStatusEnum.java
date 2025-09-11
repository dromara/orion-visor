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
 * 探针安装状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/13 23:31
 */
@Getter
@AllArgsConstructor
public enum AgentInstallStatusEnum {

    /**
     * 未安装
     */
    NOT_INSTALL(0),

    /**
     * 已安装
     */
    INSTALLED(1),

    ;

    private final Integer status;

    public static AgentInstallStatusEnum of(Integer status) {
        if (status == null) {
            return NOT_INSTALL;
        }
        for (AgentInstallStatusEnum value : AgentInstallStatusEnum.values()) {
            if (value.status.equals(status)) {
                return value;
            }
        }
        return NOT_INSTALL;
    }

}
