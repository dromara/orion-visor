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
import org.dromara.visor.module.monitor.define.operator.AlarmEventOperatorType;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventClearRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventHandleRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventSetFalseRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmEventVO;
import org.dromara.visor.module.monitor.service.AlarmEventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 告警事件 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
@Tag(name = "monitor - 告警事件服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/monitor/alarm-event")
public class AlarmEventController {

    @Resource
    private AlarmEventService alarmEventService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询告警事件")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:query')")
    public DataGrid<AlarmEventVO> getAlarmEventPage(@Validated(Page.class) @RequestBody AlarmEventQueryRequest request) {
        return alarmEventService.getAlarmEventPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @OperatorLog(AlarmEventOperatorType.HANDLE)
    @PostMapping("/handle")
    @Operation(summary = "处理告警事件")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:handle')")
    public Integer handleAlarmEvent(@Validated @RequestBody AlarmEventHandleRequest request) {
        return alarmEventService.handleAlarmEvent(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @OperatorLog(AlarmEventOperatorType.SET_FALSE)
    @PostMapping("/set-false")
    @Operation(summary = "设置为误报")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:handle')")
    public Integer setAlarmEventFalse(@Validated @RequestBody AlarmEventSetFalseRequest request) {
        return alarmEventService.setAlarmEventFalse(request);
    }

    @PostMapping("/count")
    @Operation(summary = "查询告警事件数量")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:query')")
    public Long getAlarmEventCount(@Validated @RequestBody AlarmEventQueryRequest request) {
        return alarmEventService.getAlarmEventCount(request);
    }

    @DemoDisableApi
    @OperatorLog(AlarmEventOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除告警事件")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:delete')")
    public Integer deleteAlarmEvent(@RequestParam("id") Long id) {
        return alarmEventService.deleteAlarmEventById(id);
    }

    @DemoDisableApi
    @OperatorLog(AlarmEventOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除告警事件")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:delete')")
    public Integer batchDeleteAlarmEvent(@RequestParam("idList") List<Long> idList) {
        return alarmEventService.deleteAlarmEventByIdList(idList);
    }

    @DemoDisableApi
    @OperatorLog(AlarmEventOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清理告警事件")
    @PreAuthorize("@ss.hasPermission('monitor:alarm-event:management:clear')")
    public Integer clearAlarmEvent(@Validated @RequestBody AlarmEventClearRequest request) {
        return alarmEventService.clearAlarmEvent(request);
    }

}

