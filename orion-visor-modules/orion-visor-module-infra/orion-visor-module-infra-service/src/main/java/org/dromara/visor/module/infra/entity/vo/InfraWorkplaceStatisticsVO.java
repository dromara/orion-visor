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
package org.dromara.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.common.entity.chart.LineSingleChartData;

import java.util.Date;
import java.util.List;

/**
 * 基建模块工作台统计响应
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 17:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "InfraWorkplaceStatisticsVO", description = "基建模块工作台统计响应")
public class InfraWorkplaceStatisticsVO {

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "未读消息数量")
    private Integer unreadMessageCount;

    @Schema(description = "上次登录时间")
    private Date lastLoginTime;

    @Schema(description = "当前登录会话数量")
    private Integer userSessionCount;

    @Schema(description = "系统操作数量图表")
    private LineSingleChartData operatorChart;

    @Schema(description = "用户登录日志")
    private List<LoginHistoryVO> loginHistoryList;

}
