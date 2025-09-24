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
package org.dromara.visor.module.infra.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 通知模板 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
@Module("infra:notify-template")
public class NotifyTemplateOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "notify-template:create";

    public static final String UPDATE = "notify-template:update";

    public static final String COPY = "notify-template:copy";

    public static final String DELETE = "notify-template:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建通知模板 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE, "更新通知模板 <sb>${name}</sb>"),
                new OperatorType(L, COPY, "复制通知模板 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除通知模板 <sb>${name}</sb>"),
        };
    }

}
