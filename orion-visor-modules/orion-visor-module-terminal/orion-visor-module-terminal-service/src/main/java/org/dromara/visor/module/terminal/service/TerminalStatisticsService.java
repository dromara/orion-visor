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
package org.dromara.visor.module.terminal.service;

import org.dromara.visor.common.entity.chart.LineSingleChartData;
import org.dromara.visor.module.terminal.entity.vo.TerminalWorkplaceStatisticsVO;

/**
 * 终端模块统计服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 22:24
 */
public interface TerminalStatisticsService {

    /**
     * 查询工作台统计信息
     *
     * @return data
     */
    TerminalWorkplaceStatisticsVO getWorkplaceStatisticsData();

    /**
     * 获取用户终端连接日志数量图表
     *
     * @param userId userId
     * @return data
     */
    LineSingleChartData getTerminalConnectCountChart(Long userId);

}
