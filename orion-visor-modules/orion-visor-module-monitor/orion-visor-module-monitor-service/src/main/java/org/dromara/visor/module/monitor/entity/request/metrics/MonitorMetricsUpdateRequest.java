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
package org.dromara.visor.module.monitor.entity.request.metrics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 监控指标 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-12 21:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonitorMetricsUpdateRequest", description = "监控指标 更新请求对象")
public class MonitorMetricsUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "指标名称")
    private String name;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "数据集")
    private String measurement;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "指标项")
    private String value;

    @NotBlank
    @Size(max = 8)
    @Schema(description = "单位")
    private String unit;

    @Size(max = 32)
    @Schema(description = "后缀")
    private String suffix;

    @Size(max = 128)
    @Schema(description = "指标描述")
    private String description;

}
