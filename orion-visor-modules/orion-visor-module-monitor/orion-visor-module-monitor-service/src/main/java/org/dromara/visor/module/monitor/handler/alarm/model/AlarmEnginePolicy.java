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

import java.util.List;
import java.util.Map;

/**
 * 告警引擎策略对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/19 15:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AlarmEnginePolicy", description = "告警引擎策略对象")
public class AlarmEnginePolicy {

    @Schema(description = "策略id")
    private Long id;

    @Schema(description = "策略类型")
    private String type;

    @Schema(description = "策略名称")
    private String name;

    @Schema(description = "策略规则 metricsId:rules")
    private Map<Long, List<AlarmEngineRule>> rules;

    @Schema(description = "策略推送渠道")
    private List<Long> notifyIdList;

}
