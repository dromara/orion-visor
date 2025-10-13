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
package org.dromara.visor.module.monitor.handler.alarm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 告警引擎策略规则对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/19 15:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AlarmEngineRule", description = "告警引擎策略规则对象")
public class AlarmEngineRule {

    @Schema(description = "规则id")
    private Long id;

    @Schema(description = "策略id")
    private Long policyId;

    @Schema(description = "指标id")
    private Long metricsId;

    @Schema(description = "指标标签")
    private Map<String, List<String>> tags;

    @Schema(description = "规则开关")
    private Integer ruleSwitch;

    @Schema(description = "告警级别")
    private Integer level;

    @Schema(description = "全部生效")
    private Integer allEffect;

    @Schema(description = "告警条件")
    private String triggerCondition;

    @Schema(description = "触发阈值")
    private BigDecimal threshold;

    @Schema(description = "静默时间")
    private Integer silencePeriod;

    @Schema(description = "连续触发次数")
    private Integer consecutiveCount;

}
