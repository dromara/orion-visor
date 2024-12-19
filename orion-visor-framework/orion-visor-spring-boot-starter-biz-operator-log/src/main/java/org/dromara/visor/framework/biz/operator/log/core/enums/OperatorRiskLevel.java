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
package org.dromara.visor.framework.biz.operator.log.core.enums;

import lombok.Getter;

/**
 * 操作风险等级
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/12 14:19
 */
@Getter
public enum OperatorRiskLevel {

    /**
     * 低风险
     */
    L,

    /**
     * 中风险
     */
    M,

    /**
     * 高风险
     */
    H,

    ;

}
