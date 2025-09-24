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
package org.dromara.visor.module.monitor.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.monitor.define.operator.MonitorMetricsOperatorType;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsCreateRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsQueryRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.MonitorMetricsVO;
import org.dromara.visor.module.monitor.service.MonitorMetricsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监控指标 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-12 21:31
 */
@Tag(name = "monitor - 监控指标服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/monitor/monitor-metrics")
public class MonitorMetricsController {

    @Resource
    private MonitorMetricsService monitorMetricsService;

    @DemoDisableApi
    @OperatorLog(MonitorMetricsOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-metrics:create')")
    public Long createMonitorMetrics(@Validated @RequestBody MonitorMetricsCreateRequest request) {
        return monitorMetricsService.createMonitorMetrics(request);
    }

    @DemoDisableApi
    @OperatorLog(MonitorMetricsOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-metrics:update')")
    public Integer updateMonitorMetrics(@Validated @RequestBody MonitorMetricsUpdateRequest request) {
        return monitorMetricsService.updateMonitorMetricsById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-metrics:query')")
    public List<MonitorMetricsVO> getMonitorMetricsList() {
        return monitorMetricsService.getMonitorMetricsList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-metrics:query')")
    public DataGrid<MonitorMetricsVO> getMonitorMetricsPage(@Validated(Page.class) @RequestBody MonitorMetricsQueryRequest request) {
        return monitorMetricsService.getMonitorMetricsPage(request);
    }

    @DemoDisableApi
    @OperatorLog(MonitorMetricsOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除监控指标")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:monitor-metrics:delete')")
    public Integer deleteMonitorMetrics(@RequestParam("id") Long id) {
        return monitorMetricsService.deleteMonitorMetricsById(id);
    }

}

