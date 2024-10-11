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
package com.orion.visor.module.asset.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 主机连接日志 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/2 14:37
 */
@Module("asset:host-connect-log")
public class HostConnectLogOperatorType extends InitializingOperatorTypes {

    public static final String DELETE = "host-connect-log:delete";

    public static final String CLEAR = "host-connect-log:clear";

    public static final String FORCE_OFFLINE = "host-connect-log:force-offline";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(H, DELETE, "删除主机连接记录 <sb>${count}</sb> 条"),
                new OperatorType(H, CLEAR, "清空主机连接记录 <sb>${count}</sb> 条"),
                new OperatorType(M, FORCE_OFFLINE, "强制下线主机连接 <sb>${hostName}</sb>"),
        };
    }

}
