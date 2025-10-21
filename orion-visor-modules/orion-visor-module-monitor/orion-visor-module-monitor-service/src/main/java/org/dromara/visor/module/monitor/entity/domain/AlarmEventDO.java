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
import java.util.Date;

/**
 * 监控告警事件 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "monitor_alarm_event", autoResultMap = true)
@Schema(name = "AlarmEventDO", description = "监控告警事件 实体对象")
public class AlarmEventDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "agentKey")
    @TableField("agent_key")
    private String agentKey;

    @Schema(description = "事件来源")
    @TableField("source_type")
    private String sourceType;

    @Schema(description = "事件来源id")
    @TableField("source_id")
    private Long sourceId;

    @Schema(description = "事件来源id")
    @TableField("source_info")
    private String sourceInfo;

    @Schema(description = "策略id")
    @TableField("policy_id")
    private Long policyId;

    @Schema(description = "策略规则id")
    @TableField("policy_rule_id")
    private Long policyRuleId;

    @Schema(description = "指标id")
    @TableField("metrics_id")
    private Long metricsId;

    @Schema(description = "指标数据集")
    @TableField("metrics_measurement")
    private String metricsMeasurement;

    @Schema(description = "告警标签")
    @TableField("alarm_tags")
    private String alarmTags;

    @Schema(description = "告警值")
    @TableField("alarm_value")
    private BigDecimal alarmValue;

    @Schema(description = "告警阈值")
    @TableField("alarm_threshold")
    private BigDecimal alarmThreshold;

    @Schema(description = "告警摘要")
    @TableField("alarm_info")
    private String alarmInfo;

    @Schema(description = "告警级别")
    @TableField("alarm_level")
    private Integer alarmLevel;

    @Schema(description = "告警条件")
    @TableField("trigger_condition")
    private String triggerCondition;

    @Schema(description = "连续触发次数")
    @TableField("consecutive_count")
    private Integer consecutiveCount;

    @Schema(description = "是否误报")
    @TableField("false_alarm")
    private Integer falseAlarm;

    @Schema(description = "处理状态")
    @TableField("handle_status")
    private String handleStatus;

    @Schema(description = "处理时间")
    @TableField("handle_time")
    private Date handleTime;

    @Schema(description = "处理备注")
    @TableField("handle_remark")
    private String handleRemark;

    @Schema(description = "处理人id")
    @TableField("handle_user_id")
    private Long handleUserId;

    @Schema(description = "处理人用户名")
    @TableField("handle_username")
    private String handleUsername;

}
