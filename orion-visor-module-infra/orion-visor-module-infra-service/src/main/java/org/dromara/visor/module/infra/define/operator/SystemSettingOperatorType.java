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
package org.dromara.visor.module.infra.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import static org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 系统设置 操作日志类型
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Module("infra:system-setting")
public class SystemSettingOperatorType extends InitializingOperatorTypes {

    public static final String UPDATE_TEXT = "<sb>{}</sb> - <sb>{}</sb> - <sb>{}</sb>";

    public static final String UPDATE_PARTIAL_TEXT = "<sb>{}</sb>";

    public static final String UPDATE = "system-setting:update";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, UPDATE, "更新系统设置 ${text}"),
        };
    }

}
