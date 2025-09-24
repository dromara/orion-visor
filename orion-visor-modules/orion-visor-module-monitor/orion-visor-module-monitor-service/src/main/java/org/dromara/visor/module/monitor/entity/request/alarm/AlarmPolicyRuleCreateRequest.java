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
package org.dromara.visor.module.monitor.entity.request.alarm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 监控告警规则 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 13:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AlarmPolicyRuleCreateRequest", description = "监控告警规则 创建请求对象")
public class AlarmPolicyRuleCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "策略id")
    private Long policyId;

    @NotNull
    @Schema(description = "指标id")
    private Long metricsId;

    @NotBlank
    @Schema(description = "指标标签")
    private String tags;

    @NotNull
    @Schema(description = "告警级别")
    private Integer level;

    @NotNull
    @Schema(description = "规则开关")
    private Integer ruleSwitch;

    @NotNull
    @Schema(description = "全部生效")
    private Integer allEffect;

    @NotBlank
    @Size(max = 8)
    @Schema(description = "告警条件")
    private String triggerCondition;

    @NotNull
    @Schema(description = "触发阈值")
    private BigDecimal threshold;

    @NotNull
    @Schema(description = "静默时间")
    private Integer silencePeriod;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Schema(description = "连续触发次数")
    private Integer consecutiveCount;

    @Size(max = 255)
    @Schema(description = "规则描述")
    private String description;

}
