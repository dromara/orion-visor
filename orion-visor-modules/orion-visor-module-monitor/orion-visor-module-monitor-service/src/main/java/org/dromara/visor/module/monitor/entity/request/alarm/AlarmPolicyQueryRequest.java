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
import lombok.*;
import org.dromara.visor.common.entity.BaseQueryRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 监控告警策略 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "AlarmPolicyQueryRequest", description = "监控告警策略 查询请求对象")
public class AlarmPolicyQueryRequest extends BaseQueryRequest {

    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Schema(description = "策略类型")
    private String type;

    @Size(max = 64)
    @Schema(description = "策略名称")
    private String name;

    @Size(max = 255)
    @Schema(description = "策略描述")
    private String description;

}
