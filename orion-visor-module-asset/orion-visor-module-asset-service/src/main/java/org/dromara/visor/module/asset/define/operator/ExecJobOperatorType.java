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

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 计划任务 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Module("asset:exec-job")
public class ExecJobOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "exec-job:create";

    public static final String UPDATE = "exec-job:update";

    public static final String UPDATE_STATUS = "exec-job:update-status";

    public static final String TRIGGER = "exec-job:trigger";

    public static final String UPDATE_EXEC_USER = "exec-job:update-exec-user";

    public static final String DELETE = "exec-job:delete";

    public static final String IMPORT = "exec-job:import";

    public static final String EXPORT = "exec-job:export";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建计划任务 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE, "更新计划任务 <sb>${before}</sb>"),
                new OperatorType(M, UPDATE_STATUS, "<sb>${statusName}</sb> 计划任务 <sb>${name}</sb>"),
                new OperatorType(M, TRIGGER, "手动触发计划任务 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE_EXEC_USER, "修改计划任务 <sb>${name}</sb> 执行用户为 <sb>${username}</sb>"),
                new OperatorType(H, DELETE, "删除计划任务 <sb>${name}</sb>"),
                new OperatorType(H, IMPORT, "导入计划任务 <sb>${count}</sb> 条"),
                new OperatorType(H, EXPORT, "导出计划任务 <sb>${count}</sb> 条"),
        };
    }

}
