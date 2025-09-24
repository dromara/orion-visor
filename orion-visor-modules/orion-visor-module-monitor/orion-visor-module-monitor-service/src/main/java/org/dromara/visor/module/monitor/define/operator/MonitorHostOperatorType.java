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

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 监控主机 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Module("monitor:monitor-host")
public class MonitorHostOperatorType extends InitializingOperatorTypes {

    public static final String UPDATE = "monitor-host:update";

    public static final String UPDATE_SWITCH = "monitor-host:update-switch";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, UPDATE, "更新监控配置 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE_SWITCH, "更新<sb>${count}</sb>个监控开关为 <sb>${switch}</sb>"),
        };
    }

}
