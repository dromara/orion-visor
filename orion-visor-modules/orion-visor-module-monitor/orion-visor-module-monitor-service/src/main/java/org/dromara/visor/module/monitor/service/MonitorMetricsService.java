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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.monitor.entity.domain.MonitorMetricsDO;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsCreateRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsQueryRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.MonitorMetricsVO;

import java.util.List;

/**
 * 监控指标 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-12 21:31
 */
public interface MonitorMetricsService {

    /**
     * 创建监控指标
     *
     * @param request request
     * @return id
     */
    Long createMonitorMetrics(MonitorMetricsCreateRequest request);

    /**
     * 更新监控指标
     *
     * @param request request
     * @return effect
     */
    Integer updateMonitorMetricsById(MonitorMetricsUpdateRequest request);

    /**
     * 通过缓存查询监控指标
     *
     * @return rows
     */
    List<MonitorMetricsVO> getMonitorMetricsList();

    /**
     * 分页查询监控指标
     *
     * @param request request
     * @return rows
     */
    DataGrid<MonitorMetricsVO> getMonitorMetricsPage(MonitorMetricsQueryRequest request);

    /**
     * 通过值获取监控指标名称
     *
     * @param value value
     * @return name
     */
    String getMetricName(String value);

    /**
     * 删除监控指标
     *
     * @param id id
     * @return effect
     */
    Integer deleteMonitorMetricsById(Long id);

    /**
     * 批量删除监控指标
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteMonitorMetricsByIdList(List<Long> idList);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<MonitorMetricsDO> buildQueryWrapper(MonitorMetricsQueryRequest request);

}
