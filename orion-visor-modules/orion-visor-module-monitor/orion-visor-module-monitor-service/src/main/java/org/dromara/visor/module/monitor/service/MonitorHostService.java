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
package org.dromara.visor.module.monitor.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import org.dromara.visor.common.entity.chart.TimeChartSeries;
import org.dromara.visor.module.monitor.entity.request.host.MonitorHostChartRequest;
import org.dromara.visor.module.monitor.entity.request.host.MonitorHostQueryRequest;
import org.dromara.visor.module.monitor.entity.request.host.MonitorHostSwitchUpdateRequest;
import org.dromara.visor.module.monitor.entity.request.host.MonitorHostUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostMetricsDataVO;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostVO;

import java.util.List;

/**
 * 监控主机 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
public interface MonitorHostService {

    /**
     * 分页查询监控主机
     *
     * @param request request
     * @return rows
     */
    DataGrid<MonitorHostVO> getMonitorHostPage(MonitorHostQueryRequest request);

    /**
     * 获取监控主机指标数据
     *
     * @param agentKeyList agentKeyList
     * @return metrics
     */
    List<MonitorHostMetricsDataVO> getMonitorHostMetrics(List<String> agentKeyList);

    /**
     * 获取监控主机图表数据
     *
     * @param request request
     * @return series
     */
    List<TimeChartSeries> getMonitorHostChart(MonitorHostChartRequest request);

    /**
     * 更新监控主机
     *
     * @param request request
     * @return effect
     */
    Integer updateMonitorHostById(MonitorHostUpdateRequest request);

    /**
     * 更新监控主机告警开关
     *
     * @param request request
     * @return effect
     */
    Integer updateMonitorHostAlarmSwitch(MonitorHostSwitchUpdateRequest request);

    /**
     * 删除监控主机
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    Integer deleteByHostIdList(List<Long> hostIdList);

}
