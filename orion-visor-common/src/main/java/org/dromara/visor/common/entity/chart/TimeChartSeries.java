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
package org.dromara.visor.common.entity.chart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 时序图系列
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/3 21:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TimeChartSeries", description = "时序图系列")
public class TimeChartSeries {

    @Schema(description = "name")
    private String name;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "tags")
    private Map<String, Object> tags;

    @Schema(description = "数据 [0]timestampMills [1]value")
    private List<List<Object>> data;

}
