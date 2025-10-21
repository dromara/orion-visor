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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.common.entity.BaseQueryRequest;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 监控告警事件 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "AlarmEventQueryRequest", description = "监控告警事件 查询请求对象")
public class AlarmEventQueryRequest extends BaseQueryRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "agentKey")
    private String agentKey;

    @Schema(description = "事件来源")
    private String sourceType;

    @Schema(description = "事件来源id")
    private Long sourceId;

    @Schema(description = "策略id")
    private Long policyId;

    @Schema(description = "指标id")
    private Long metricsId;

    @Size(max = 64)
    @Schema(description = "指标数据集")
    private String metricsMeasurement;

    @Schema(description = "告警级别")
    private Integer alarmLevel;

    @Schema(description = "是否误报")
    private Integer falseAlarm;

    @Size(max = 16)
    @Schema(description = "处理状态")
    private String handleStatus;

    @Size(max = 512)
    @Schema(description = "处理备注")
    private String handleRemark;

    @Schema(description = "处理人id")
    private Long handleUserId;

    @Schema(description = "创建时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] createTimeRange;

}
