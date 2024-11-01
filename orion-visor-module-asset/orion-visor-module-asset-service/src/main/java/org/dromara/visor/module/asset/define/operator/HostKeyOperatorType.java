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
 * 主机密钥 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-key")
public class HostKeyOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "host-key:create";

    public static final String UPDATE = "host-key:update";

    public static final String DELETE = "host-key:delete";

    public static final String GRANT = "host-key:grant";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(OperatorRiskLevel.L, CREATE, "创建主机密钥 <sb>${name}</sb>"),
                new OperatorType(OperatorRiskLevel.L, UPDATE, "修改主机密钥 <sb>${name}</sb>"),
                new OperatorType(OperatorRiskLevel.H, DELETE, "删除主机密钥 <sb>${name}</sb>"),
                new OperatorType(OperatorRiskLevel.H, GRANT, "将主机密钥权限授予 <sb>${grantType}</sb> <sb>${grantName}</sb>"),
        };
    }

}
