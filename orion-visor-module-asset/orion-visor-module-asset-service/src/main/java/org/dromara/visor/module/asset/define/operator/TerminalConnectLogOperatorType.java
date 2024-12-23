/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 终端连接日志 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/2 14:37
 */
@Module("asset:terminal-connect-log")
public class TerminalConnectLogOperatorType extends InitializingOperatorTypes {

    public static final String DELETE = "terminal-connect-log:delete";

    public static final String CLEAR = "terminal-connect-log:clear";

    public static final String FORCE_OFFLINE = "terminal-connect-log:force-offline";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(H, DELETE, "删除终端连接记录 <sb>${count}</sb> 条"),
                new OperatorType(H, CLEAR, "清空终端连接记录 <sb>${count}</sb> 条"),
                new OperatorType(M, FORCE_OFFLINE, "强制下线终端连接 <sb>${hostName}</sb>"),
        };
    }

}
