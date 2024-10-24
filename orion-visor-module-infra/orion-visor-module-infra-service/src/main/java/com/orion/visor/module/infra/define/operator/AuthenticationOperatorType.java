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
package com.orion.visor.module.infra.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;

/**
 * 认证服务 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 19:06
 */
@Module("infra:authentication")
public class AuthenticationOperatorType extends InitializingOperatorTypes {

    public static final String LOGIN = "authentication:login";

    public static final String LOGOUT = "authentication:logout";

    public static final String UPDATE_PASSWORD = "authentication:update-password";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, LOGIN, "登录系统"),
                new OperatorType(L, LOGOUT, "登出系统"),
                new OperatorType(L, UPDATE_PASSWORD, "修改密码"),
        };
    }

}
