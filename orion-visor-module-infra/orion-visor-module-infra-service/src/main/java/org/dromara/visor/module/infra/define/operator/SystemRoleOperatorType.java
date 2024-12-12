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
 * 系统角色 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("infra:system-role")
public class SystemRoleOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "system-role:create";

    public static final String UPDATE = "system-role:update";

    public static final String UPDATE_STATUS = "system-role:update-status";

    public static final String DELETE = "system-role:delete";

    public static final String GRANT_MENU = "system-role:grant-menu";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建角色 <sb>${name}(${code})</sb>"),
                new OperatorType(M, UPDATE, "修改角色 <sb>${name}(${code})</sb>"),
                new OperatorType(M, UPDATE_STATUS, "修改角色状态 <sb>${name}(${code})</sb> - <sb>${statusName}</sb>"),
                new OperatorType(H, DELETE, "删除角色 <sb>${name}(${code})</sb>"),
                new OperatorType(M, GRANT_MENU, "分配角色菜单 <sb>${name}(${code})</sb>"),
        };
    }

}
