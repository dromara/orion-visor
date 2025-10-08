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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.monitor.define.operator.AlarmPolicyOperatorType;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateSwitchRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyRuleVO;
import org.dromara.visor.module.monitor.service.AlarmPolicyRuleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监控告警规则 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 13:39
 */
@Tag(name = "monitor - 监控告警规则服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/monitor/alarm-policy-rule")
public class AlarmPolicyRuleController {

    @Resource
    private AlarmPolicyRuleService alarmPolicyRuleService;

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.CREATE_RULE)
    @PostMapping("/create")
    @Operation(summary = "创建监控告警规则")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:update-rule')")
    public Long createAlarmPolicyRule(@Validated @RequestBody AlarmPolicyRuleCreateRequest request) {
        return alarmPolicyRuleService.createAlarmPolicyRule(request);
    }

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.UPDATE_RULE)
    @PutMapping("/update")
    @Operation(summary = "更新监控告警规则")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:update-rule')")
    public Integer updateAlarmPolicyRuleById(@Validated @RequestBody AlarmPolicyRuleUpdateRequest request) {
        return alarmPolicyRuleService.updateAlarmPolicyRuleById(request);
    }

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.UPDATE_RULE_SWITCH)
    @PutMapping("/update-switch")
    @Operation(summary = "更新监控告警规则")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:update-rule')")
    public Integer updateAlarmPolicyRuleSwitch(@Validated @RequestBody AlarmPolicyRuleUpdateSwitchRequest request) {
        return alarmPolicyRuleService.updateAlarmPolicyRuleSwitch(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部监控告警规则")
    @Parameter(name = "policyId", description = "policyId", required = true)
    @Parameter(name = "measurement", description = "measurement")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:query')")
    public List<AlarmPolicyRuleVO> getAlarmPolicyRuleList(@RequestParam("policyId") Long policyId,
                                                          @RequestParam(value = "measurement", required = false) String measurement) {
        return alarmPolicyRuleService.getAlarmPolicyRuleList(policyId, measurement);
    }

    @DemoDisableApi
    @OperatorLog(AlarmPolicyOperatorType.DELETE_RULE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除监控告警规则")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alarm-policy:update-rule')")
    public Integer deleteAlarmPolicyRule(@RequestParam("id") Long id) {
        return alarmPolicyRuleService.deleteAlarmPolicyRuleById(id);
    }

}

