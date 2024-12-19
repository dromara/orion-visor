/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.biz.operator.log.core.model;

import lombok.Getter;
import org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel;

/**
 * 操作类型定义
 * <p>
 * 因为枚举需要实现 注解中不可以使用 则需要使用对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 10:29
 */
@Getter
public class OperatorType {

    /**
     * 风险等级
     */
    private final OperatorRiskLevel riskLevel;

    /**
     * 模块
     */
    private String module;

    /**
     * 类型
     */
    private final String type;

    /**
     * 模板
     */
    private final String template;

    public OperatorType(OperatorRiskLevel riskLevel, String type, String template) {
        this(riskLevel, null, type, template);
    }

    public OperatorType(OperatorRiskLevel riskLevel, String module, String type, String template) {
        this.riskLevel = riskLevel;
        this.module = module;
        this.type = type;
        this.template = template;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
