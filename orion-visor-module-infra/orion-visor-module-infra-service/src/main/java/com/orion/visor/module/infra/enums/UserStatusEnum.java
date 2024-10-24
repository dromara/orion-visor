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
package com.orion.visor.module.infra.enums;

import com.orion.visor.framework.common.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:35
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /**
     * 0 停用
     */
    DISABLED(0),

    /**
     * 1 启用
     */
    ENABLED(1),

    ;

    private final Integer status;

    public static UserStatusEnum of(Integer status) {
        if (status == null) {
            return null;
        }
        for (UserStatusEnum value : values()) {
            if (value.status.equals(status)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 检查用户状态
     *
     * @param status status
     */
    public static void checkUserStatus(Integer status) {
        if (UserStatusEnum.DISABLED.getStatus().equals(status)) {
            // 禁用状态
            throw ErrorCode.USER_DISABLED.exception();
        }
    }

}
