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
package org.dromara.visor.module.monitor.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.math.BigDecimal;

/**
 * 监控告警规则 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 13:39
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "monitor_alarm_policy_rule", autoResultMap = true)
@Schema(name = "AlarmPolicyRuleDO", description = "监控告警规则 实体对象")
public class AlarmPolicyRuleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "策略id")
    @TableField("policy_id")
    private Long policyId;

    @Schema(description = "指标id")
    @TableField("metrics_id")
    private Long metricsId;

    @Schema(description = "指标数据集")
    @TableField("metrics_measurement")
    private String metricsMeasurement;

    @Schema(description = "指标标签")
    @TableField("tags")
    private String tags;

    @Schema(description = "规则开关")
    @TableField("rule_switch")
    private Integer ruleSwitch;

    @Schema(description = "全部生效")
    @TableField("all_effect")
    private Integer allEffect;

    @Schema(description = "告警级别")
    @TableField("level")
    private Integer level;

    @Schema(description = "告警条件")
    @TableField("trigger_condition")
    private String triggerCondition;

    @Schema(description = "触发阈值")
    @TableField("threshold")
    private BigDecimal threshold;

    @Schema(description = "静默时间")
    @TableField("silence_period")
    private Integer silencePeriod;

    @Schema(description = "连续触发次数")
    @TableField("consecutive_count")
    private Integer consecutiveCount;

    @Schema(description = "规则描述")
    @TableField("description")
    private String description;

}
