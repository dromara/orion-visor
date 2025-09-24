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
package org.dromara.visor.module.terminal.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.validator.group.Id;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.terminal.define.operator.TerminalConnectLogOperatorType;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalConnectLogClearRequest;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalConnectLogQueryRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalConnectLogVO;
import org.dromara.visor.module.terminal.service.TerminalConnectLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 终端连接日志 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Tag(name = "terminal - 终端连接日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/terminal/terminal-connect-log")
public class TerminalConnectLogController {

    @Resource
    private TerminalConnectLogService terminalConnectLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询终端连接日志")
    @PreAuthorize("@ss.hasPermission('terminal:terminal-connect-log:management:query')")
    public DataGrid<TerminalConnectLogVO> getTerminalConnectLogPage(@Validated(Page.class) @RequestBody TerminalConnectLogQueryRequest request) {
        return terminalConnectLogService.getTerminalConnectLogPage(request);
    }

    @PostMapping("/count")
    @Operation(summary = "查询终端连接日志数量")
    @PreAuthorize("@ss.hasPermission('terminal:terminal-connect-log:management:query')")
    public Long getTerminalConnectLogCount(@Validated @RequestBody TerminalConnectLogQueryRequest request) {
        return terminalConnectLogService.getTerminalConnectLogCount(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/sessions")
    @Operation(summary = "查询全部终端会话")
    @PreAuthorize("@ss.hasPermission('terminal:terminal-connect-session:management:query')")
    public List<TerminalConnectLogVO> getTerminalConnectSessions(@Validated @RequestBody TerminalConnectLogQueryRequest request) {
        return terminalConnectLogService.getTerminalConnectSessions(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/latest-connect")
    @Operation(summary = "查询用户最近连接的主机")
    public Set<Long> getLatestConnectHostId(@RequestBody TerminalConnectLogQueryRequest request) {
        return terminalConnectLogService.getLatestConnectHostId(request);
    }

    @OperatorLog(TerminalConnectLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除终端连接日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('terminal:terminal-connect-log:management:delete')")
    public Integer deleteTerminalConnectLog(@RequestParam("idList") List<Long> idList) {
        return terminalConnectLogService.deleteTerminalConnectLog(idList);
    }

    @OperatorLog(TerminalConnectLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清理终端连接日志")
    @PreAuthorize("@ss.hasPermission('terminal:terminal-connect-log:management:clear')")
    public Integer clearTerminalConnectLog(@Validated @RequestBody TerminalConnectLogClearRequest request) {
        return terminalConnectLogService.clearTerminalConnectLog(request);
    }

    @DemoDisableApi
    @OperatorLog(TerminalConnectLogOperatorType.FORCE_OFFLINE)
    @PutMapping("/force-offline")
    @Operation(summary = "强制断开终端")
    @PreAuthorize("@ss.hasAnyPermission('terminal:terminal-connect-log:management:force-offline', 'terminal:terminal-connect-session:management:force-offline')")
    public Boolean forceOffline(@Validated(Id.class) @RequestBody TerminalConnectLogQueryRequest request) {
        terminalConnectLogService.forceOffline(request);
        return true;
    }

}

