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

import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.visor.module.infra.entity.dto.LoginTokenDTO;
import com.orion.visor.module.infra.entity.dto.LoginTokenIdentityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * 登录 token 状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 16:15
 */
@Getter
@AllArgsConstructor
public enum LoginTokenStatusEnum {

    /**
     * 正常
     */
    OK(0),

    /**
     * 已在其他设备登录
     */
    OTHER_DEVICE(1) {
        @Override
        public RuntimeException toException(LoginTokenDTO token) {
            LoginTokenIdentityDTO override = token.getOverride();
            return ErrorCode.USER_OTHER_DEVICE_LOGIN.exception(
                    Dates.format(new Date(override.getLoginTime()), Dates.MD_HM),
                    override.getAddress(),
                    override.getLocation());
        }

    },

    /**
     * 强制下线
     */
    SESSION_OFFLINE(2) {
        @Override
        public RuntimeException toException(LoginTokenDTO token) {
            LoginTokenIdentityDTO override = token.getOverride();
            return ErrorCode.USER_OFFLINE.exception(
                    Dates.format(new Date(override.getLoginTime()), Dates.MD_HM),
                    override.getAddress(),
                    override.getLocation());
        }

    },

    ;

    private final Integer status;

    /**
     * 获取异常信息
     *
     * @param token token
     * @return exception
     */
    public RuntimeException toException(LoginTokenDTO token) {
        return null;
    }

    public static LoginTokenStatusEnum of(Integer status) {
        if (status == null) {
            return OK;
        }
        for (LoginTokenStatusEnum value : values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return OK;
    }

}
