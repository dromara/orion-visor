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
import org.dromara.visor.module.monitor.define.operator.AlarmPolicyOperatorType;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCopyRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyVO;
import org.dromara.visor.module.monitor.service.AlarmPolicyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监控告警策略 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
@Tag(name = "monitor - 监控告警策略服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/monitor/alarm-policy")
public class AlarmPolicyController {

    @Resource
    private AlarmPolicyService alarmPolicyService;

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建监控告警策略")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:create')")
    public Long createAlarmPolicy(@Validated @RequestBody AlarmPolicyCreateRequest request) {
        return alarmPolicyService.createAlarmPolicy(request);
    }

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.COPY)
    @PostMapping("/copy")
    @Operation(summary = "复制监控告警策略")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:copy')")
    public Long copyAlarmPolicy(@Validated @RequestBody AlarmPolicyCopyRequest request) {
        return alarmPolicyService.copyAlarmPolicy(request);
    }

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新监控告警策略")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:update')")
    public Integer updateAlarmPolicy(@Validated @RequestBody AlarmPolicyUpdateRequest request) {
        return alarmPolicyService.updateAlarmPolicyById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询告警策略")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:query')")
    public AlarmPolicyVO getAlarmPolicy(@RequestParam("id") Long id) {
        return alarmPolicyService.getAlarmPolicyById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部监控告警策略")
    @Parameter(name = "type", description = "type", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:query')")
    public List<AlarmPolicyVO> getAlarmPolicyList(@RequestParam("type") String type) {
        return alarmPolicyService.getAlarmPolicyListByCache(type);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询监控告警策略")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:query')")
    public DataGrid<AlarmPolicyVO> getAlarmPolicyPage(@Validated(Page.class) @RequestBody AlarmPolicyQueryRequest request) {
        return alarmPolicyService.getAlarmPolicyPage(request);
    }

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除监控告警策略")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:delete')")
    public Integer deleteAlarmPolicy(@RequestParam("id") Long id) {
        return alarmPolicyService.deleteAlarmPolicyById(id);
    }

}

