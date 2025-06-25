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
package org.dromara.visor.module.exec.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 批量执行日志 操作记录类型
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Module("exec:exec-command-log")
public class ExecCommandLogOperatorType extends InitializingOperatorTypes {

    public static final String DELETE = "exec-command-log:delete";

    public static final String DELETE_HOST = "exec-command-log:delete-host";

    public static final String CLEAR = "exec-command-log:clear";

    public static final String DOWNLOAD = "exec-command-log:download";

    public static final String INTERRUPT = "exec-command-log:interrupt";

    public static final String INTERRUPT_HOST = "exec-command-log:interrupt-host";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(H, DELETE, "删除批量执行日志 <sb>${count}</sb> 条"),
                new OperatorType(H, DELETE_HOST, "删除批量执行主机日志 <sb>${logId}</sb> <sb>${hostName}</sb>"),
                new OperatorType(H, CLEAR, "清理批量执行日志 <sb>${count}</sb> 条"),
                new OperatorType(L, DOWNLOAD, "下载批量执行日志 <sb>${logId}</sb> <sb>${hostName}</sb>"),
                new OperatorType(M, INTERRUPT, "中断批量执行命令"),
                new OperatorType(M, INTERRUPT_HOST, "中断批量执行主机命令 <sb>${logId}</sb> <sb>${hostName}</sb>"),
        };
    }

}
