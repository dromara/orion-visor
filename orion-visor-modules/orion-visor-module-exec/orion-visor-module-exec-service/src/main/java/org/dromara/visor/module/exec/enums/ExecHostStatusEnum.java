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
package org.dromara.visor.module.exec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批量执行主机状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 17:08
 */
@Getter
@AllArgsConstructor
public enum ExecHostStatusEnum {

    /**
     * 等待中
     */
    WAITING(true),

    /**
     * 执行中
     */
    RUNNING(true),

    /**
     * 执行完成
     */
    COMPLETED(false),

    /**
     * 执行失败
     */
    FAILED(false),

    /**
     * 执行超时
     */
    TIMEOUT(false),

    /**
     * 中断执行
     */
    INTERRUPTED(false),

    ;

    private final boolean closeable;

    public static final List<String> CLOSEABLE_STATUS = Arrays.stream(ExecHostStatusEnum.values())
            .filter(ExecHostStatusEnum::isCloseable)
            .map(Enum::name)
            .collect(Collectors.toList());

    public static ExecHostStatusEnum of(String status) {
        if (status == null) {
            return null;
        }
        for (ExecHostStatusEnum value : values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
