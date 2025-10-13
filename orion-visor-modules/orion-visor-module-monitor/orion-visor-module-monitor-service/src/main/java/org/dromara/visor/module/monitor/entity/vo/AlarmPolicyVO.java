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
package org.dromara.visor.module.monitor.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 监控告警策略 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AlarmPolicyVO", description = "监控告警策略 视图响应对象")
public class AlarmPolicyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "策略类型")
    private String type;

    @Schema(description = "策略名称")
    private String name;

    @Schema(description = "策略描述")
    private String description;

    @Schema(description = "规则数")
    private Integer ruleCount;

    @Schema(description = "主机数")
    private Integer hostCount;

    @Schema(description = "今日触发次数")
    private Integer todayTriggerCount;

    @Schema(description = "一周触发次数")
    private Integer weekTriggerCount;

    @Schema(description = "通知模板")
    private List<Long> notifyIdList;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "修改人")
    private String updater;

}
