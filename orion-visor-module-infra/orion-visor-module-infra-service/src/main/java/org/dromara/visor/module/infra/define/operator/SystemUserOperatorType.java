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
package org.dromara.visor.module.infra.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 系统用户 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("infra:system-user")
public class SystemUserOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "system-user:create";

    public static final String UPDATE = "system-user:update";

    public static final String UPDATE_STATUS = "system-user:update-status";

    public static final String GRANT_ROLE = "system-user:grant-role";

    public static final String RESET_PASSWORD = "system-user:reset-password";

    public static final String DELETE = "system-user:delete";

    public static final String OFFLINE = "system-user:offline";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建用户 <sb>${username}</sb>"),
                new OperatorType(M, UPDATE, "修改用户 <sb>${username}</sb>"),
                new OperatorType(M, UPDATE_STATUS, "修改用户状态 <sb>${username}</sb> - <sb>${statusName}</sb>"),
                new OperatorType(M, GRANT_ROLE, "分配用户角色 <sb>${username}</sb>"),
                new OperatorType(H, RESET_PASSWORD, "重置用户密码 <sb>${username}</sb>"),
                new OperatorType(H, DELETE, "删除用户 <sb>${username}</sb>"),
                new OperatorType(M, OFFLINE, "下线用户会话 <sb>${username}</sb>"),
        };
    }

}
