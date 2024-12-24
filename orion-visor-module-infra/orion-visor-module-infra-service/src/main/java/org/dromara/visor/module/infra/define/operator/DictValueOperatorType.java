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
 * 字典配置值 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Module("infra:dict-value")
public class DictValueOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "dict-value:create";

    public static final String UPDATE = "dict-value:update";

    public static final String DELETE = "dict-value:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建字典配置值 <sb>${keyName}</sb> - <sb>${label}</sb> | <sb>${value}</sb>"),
                new OperatorType(M, UPDATE, "更新字典配置值 <sb>${keyName}</sb> - <sb>${label}</sb> | <sb>${value}</sb>"),
                new OperatorType(H, DELETE, "删除字典配置值 <sb>${value}</sb>"),
        };
    }

}
