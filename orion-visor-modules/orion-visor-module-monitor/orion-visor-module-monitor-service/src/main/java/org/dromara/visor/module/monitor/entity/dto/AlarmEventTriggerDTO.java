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
package org.dromara.visor.module.monitor.entity.dto;

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
 * 监控告警触发记录
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
@Schema(name = "AlarmEventTriggerDTO", description = "监控告警触发记录")
public class AlarmEventTriggerDTO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "agentKey")
    private String agentKey;

    @Schema(description = "事件来源")
    private String sourceType;

    @Schema(description = "事件来源id")
    private Long sourceId;

    @Schema(description = "事件来源id")
    private String sourceInfo;

    @Schema(description = "策略id")
    private Long policyId;

    @Schema(description = "策略规则id")
    private Long policyRuleId;

    @Schema(description = "指标id")
    private Long metricsId;

    @Schema(description = "指标数据集")
    private String metricsMeasurement;

    @Schema(description = "告警标签")
    private String alarmTags;

    @Schema(description = "告警值")
    private BigDecimal alarmValue;

    @Schema(description = "告警阈值")
    private BigDecimal alarmThreshold;

    @Schema(description = "告警摘要")
    private String alarmInfo;

    @Schema(description = "告警级别")
    private Integer alarmLevel;

    @Schema(description = "告警条件")
    private String triggerCondition;

    @Schema(description = "连续触发次数")
    private Integer consecutiveCount;

    @Schema(description = "处理状态")
    private String handleStatus;

    @Schema(description = "处理时间")
    private Date handleTime;

    @Schema(description = "处理备注")
    private String handleRemark;

    @Schema(description = "处理人id")
    private Long handleUserId;

    @Schema(description = "处理人用户名")
    private String handleUsername;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

}
