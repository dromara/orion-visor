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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 监控主机上下文对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/13 23:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonitorHostContextDTO", description = "监控主机上下文对象")
public class MonitorHostContextDTO implements Serializable {

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "策略id")
    private Long policyId;

    @Schema(description = "agentKey")
    private String agentKey;

    @Schema(description = "告警开关")
    private Integer alarmSwitch;

    @Schema(description = "负责人id")
    private Long ownerUserId;

    @Schema(description = "负责人用户名")
    private String ownerUsername;

    @Schema(description = "主机配置")
    private MonitorHostConfigDTO config;

}
