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
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.terminal.define.operator.TerminalFileLogOperatorType;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalFileLogQueryRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalFileLogVO;
import org.dromara.visor.module.terminal.service.TerminalFileLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 终端文件日志操作服务 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Tag(name = "terminal - 终端文件日志操作服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/terminal/terminal-file-log")
public class TerminalFileLogController {

    @Resource
    private TerminalFileLogService terminalFileLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询终端文件操作日志")
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:query', 'terminal:terminal-file-log:management:query')")
    public DataGrid<TerminalFileLogVO> getTerminalFileLogPage(@Validated(Page.class) @RequestBody TerminalFileLogQueryRequest request) {
        return terminalFileLogService.getTerminalFileLogPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/count")
    @Operation(summary = "查询终端文件操作日志数量")
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:query', 'terminal:terminal-file-log:management:query')")
    public Long getTerminalFileLogCount(@Validated @RequestBody TerminalFileLogQueryRequest request) {
        return terminalFileLogService.getTerminalFileLogCount(request);
    }

    @OperatorLog(TerminalFileLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除终端文件操作日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:delete', 'terminal:terminal-file-log:management:delete')")
    public Integer deleteTerminalFileLog(@RequestParam("idList") List<Long> idList) {
        return terminalFileLogService.deleteTerminalFileLog(idList);
    }

}
