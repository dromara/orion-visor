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
package org.dromara.visor.module.exec.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.common.entity.chart.LineSingleChartData;

import java.util.List;

/**
 * 执行模块工作台统计响应
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/26 15:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecWorkplaceStatisticsVO", description = "执行模块工作台统计响应")
public class ExecWorkplaceStatisticsVO {

    @Schema(description = "执行的计划任务数量")
    private Integer execJobCount;

    @Schema(description = "今日批量执行数量")
    private Integer todayExecCommandCount;

    @Schema(description = "7日批量执行次数")
    private Integer weekExecCommandCount;

    @Schema(description = "批量执行数量图表")
    private LineSingleChartData execCommandChart;

    @Schema(description = "批量执行记录")
    private List<ExecLogVO> execLogList;

}
