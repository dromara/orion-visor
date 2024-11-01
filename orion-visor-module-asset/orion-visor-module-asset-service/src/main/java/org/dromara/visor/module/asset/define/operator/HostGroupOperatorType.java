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
package org.dromara.visor.module.asset.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

/**
 * 主机分组 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-group")
public class HostGroupOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "host-group:create";

    public static final String RENAME = "host-group:rename";

    public static final String MOVE = "host-group:move";

    public static final String DELETE = "host-group:delete";

    public static final String UPDATE_REL = "host-group:update-rel";

    public static final String GRANT = "host-group:grant";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(OperatorRiskLevel.L, CREATE, "创建主机分组 <sb>${name}</sb>"),
                new OperatorType(OperatorRiskLevel.L, RENAME, "重命名主机分组 <sb>${before}</sb> -> <sb>${name}</sb>"),
                new OperatorType(OperatorRiskLevel.L, MOVE, "移动主机分组 <sb>${source}</sb> 到 <sb>${target}(${position})</sb>"),
                new OperatorType(OperatorRiskLevel.H, DELETE, "删除主机分组 <sb>${groupName}</sb>"),
                new OperatorType(OperatorRiskLevel.M, UPDATE_REL, "修改分组内主机 <sb>${groupName}</sb>"),
                new OperatorType(OperatorRiskLevel.H, GRANT, "将主机分组权限授予 <sb>${grantType}</sb> <sb>${grantName}</sb>"),
        };
    }

}
