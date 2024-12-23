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
package org.dromara.visor.module.asset.enums;

/**
 * 批量执行来源
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 16:00
 */
public enum ExecSourceEnum {

    /**
     * 批量执行
     */
    BATCH,

    /**
     * 计划任务
     */
    JOB,

    ;

    public static ExecSourceEnum of(String source) {
        if (source == null) {
            return null;
        }
        for (ExecSourceEnum value : values()) {
            if (value.name().equals(source)) {
                return value;
            }
        }
        return null;
    }

}
