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
import org.dromara.visor.common.entity.chart.TimeChartSeries;
import org.dromara.visor.common.validator.group.Key;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.monitor.context.MonitorAgentContext;
import org.dromara.visor.module.monitor.define.operator.MonitorHostOperatorType;
import org.dromara.visor.module.monitor.entity.dto.AgentMetricsDataDTO;
import org.dromara.visor.module.monitor.entity.request.host.*;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostMetricsDataVO;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostVO;
import org.dromara.visor.module.monitor.service.MonitorHostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监控主机 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Tag(name = "monitor - 监控主机服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/monitor/monitor-host")
public class MonitorHostController {

    @Resource
    private MonitorHostService monitorHostService;

    @Resource
    private MonitorAgentContext monitorAgentContext;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询监控主机")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-host:query')")
    public DataGrid<MonitorHostVO> getMonitorHostPage(@Validated(Page.class) @RequestBody MonitorHostQueryRequest request) {
        return monitorHostService.getMonitorHostPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/override")
    @Operation(summary = "查询监控主机概览")
    @Parameter(name = "agentKey", description = "agentKey", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:monitor-host:query')")
    public AgentMetricsDataDTO getMonitorHostOverride(@RequestParam("agentKey") String agentKey) {
        return monitorAgentContext.getAgentMetrics(agentKey);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/metrics")
    @Operation(summary = "查询监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-host:query')")
    public List<MonitorHostMetricsDataVO> getMonitorHostMetrics(@Validated(Key.class) @RequestBody MonitorHostQueryRequest request) {
        return monitorHostService.getMonitorHostMetrics(request.getAgentKeys());
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/chart")
    @Operation(summary = "查询监控指标")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-host:query')")
    public List<TimeChartSeries> getMonitorHostChart(@Validated @RequestBody MonitorHostChartRequest request) {
        return monitorHostService.getMonitorHostChart(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/host-tags")
    @Operation(summary = "查询监控告警标签")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-host:query')")
    public List<String> getMonitorHostTags(@RequestBody MonitorHostQueryTagRequest request) {
        return monitorHostService.getMonitorHostTags(request);
    }

    @DemoDisableApi
    @OperatorLog(MonitorHostOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新监控主机")
    @PreAuthorize("@ss.hasPermission('monitor:monitor-host:update')")
    public Integer updateMonitorHost(@Validated @RequestBody MonitorHostUpdateRequest request) {
        return monitorHostService.updateMonitorHostById(request);
    }

    @DemoDisableApi
    @OperatorLog(MonitorHostOperatorType.UPDATE_SWITCH)
    @PutMapping("/update-switch")
    @Operation(summary = "更新监控主机告警开关")
    @PreAuthorize("@ss.hasAnyPermission('monitor:monitor-host:update', 'monitor:monitor-host:update-switch')")
    public Integer updateMonitorHostAlarmSwitch(@Validated @RequestBody MonitorHostSwitchUpdateRequest request) {
        return monitorHostService.updateMonitorHostAlarmSwitch(request);
    }

}
