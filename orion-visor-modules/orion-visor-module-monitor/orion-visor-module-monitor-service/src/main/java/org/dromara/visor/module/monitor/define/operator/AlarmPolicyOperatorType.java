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
package org.dromara.visor.module.monitor.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 监控告警策略 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
@Module("monitor:alarm-policy")
public class AlarmPolicyOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "alarm-policy:create";

    public static final String UPDATE = "alarm-policy:update";

    public static final String COPY = "alarm-policy:copy";

    public static final String DELETE = "alarm-policy:delete";

    public static final String CREATE_RULE = "alarm-policy:create-rule";

    public static final String UPDATE_RULE = "alarm-policy:update-rule";

    public static final String UPDATE_RULE_SWITCH = "alarm-policy:update-rule-switch";

    public static final String DELETE_RULE = "alarm-policy:delete-rule";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建监控告警策略 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE, "更新监控告警策略 <sb>${name}</sb>"),
                new OperatorType(L, COPY, "复制监控告警策略 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除监控告警策略 <sb>${name}</sb>"),
                new OperatorType(L, CREATE_RULE, "创建监控告警规则"),
                new OperatorType(M, UPDATE_RULE, "更新监控告警规则"),
                new OperatorType(M, UPDATE_RULE_SWITCH, "更新监控告警规则开关 <sb>${switch}</sb>"),
                new OperatorType(H, DELETE_RULE, "删除监控告警规则"),
        };
    }

}
