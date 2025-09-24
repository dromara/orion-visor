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

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;

/**
 * 监控告警记录 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
@Module("monitor:alarm-event")
public class AlarmEventOperatorType extends InitializingOperatorTypes {

    public static final String HANDLE = "alarm-event:handle";

    public static final String SET_FALSE = "alarm-event:set-false";

    public static final String DELETE = "alarm-event:delete";

    public static final String CLEAR = "alarm-event:clear";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, HANDLE, "设置告警状态为 ${status} <sb>${count}</sb> 条"),
                new OperatorType(L, SET_FALSE, "设置告警记录为误报 <sb>${count}</sb> 条"),
                new OperatorType(H, DELETE, "删除告警记录 <sb>${count}</sb> 条"),
                new OperatorType(H, CLEAR, "清理告警记录 <sb>${count}</sb> 条"),
        };
    }

}
