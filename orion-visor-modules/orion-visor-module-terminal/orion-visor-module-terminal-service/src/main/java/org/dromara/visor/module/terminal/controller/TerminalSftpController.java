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
import org.dromara.visor.framework.web.core.annotation.IgnoreWrapper;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalSftpLogQueryRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalSftpLogVO;
import org.dromara.visor.module.terminal.service.TerminalSftpService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * SFTP 操作服务 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Tag(name = "terminal - SFTP 操作服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/terminal/terminal-sftp")
public class TerminalSftpController {

    @Resource
    private TerminalSftpService terminalSftpService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query-log")
    @Operation(summary = "分页查询 SFTP 操作日志")
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:query', 'terminal:terminal-sftp-log:management:query')")
    public DataGrid<TerminalSftpLogVO> getTerminalSftpLogPage(@Validated(Page.class) @RequestBody TerminalSftpLogQueryRequest request) {
        return terminalSftpService.getTerminalSftpLogPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/log-count")
    @Operation(summary = "查询 SFTP 操作日志数量")
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:query', 'terminal:terminal-sftp-log:management:query')")
    public Long getTerminalSftpLogCount(@Validated @RequestBody TerminalSftpLogQueryRequest request) {
        return terminalSftpService.getTerminalSftpLogCount(request);
    }

    @OperatorLog(TerminalOperatorType.DELETE_SFTP_LOG)
    @DeleteMapping("/delete-log")
    @Operation(summary = "删除 SFTP 操作日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:delete', 'terminal:terminal-sftp-log:management:delete')")
    public Integer deleteTerminalSftpLog(@RequestParam("idList") List<Long> idList) {
        return terminalSftpService.deleteTerminalSftpLog(idList);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-content")
    @Operation(summary = "获取文件内容")
    @Parameter(name = "token", description = "token", required = true)
    public void getFileContentByToken(@RequestParam("token") String token, HttpServletResponse response) throws Exception {
        terminalSftpService.getFileContentByToken(token, response);
    }

    @PostMapping("/set-content")
    @Operation(summary = "设置文件内容")
    @Parameter(name = "token", description = "token", required = true)
    @Parameter(name = "file", description = "file", required = true)
    public Boolean setFileContentByToken(@RequestParam("token") String token,
                                         @RequestParam("file") MultipartFile file) throws Exception {
        terminalSftpService.setFileContentByToken(token, file);
        return true;
    }

    @PermitAll
    @IgnoreWrapper
    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/download")
    @Operation(summary = "下载文件")
    @Parameter(name = "channelId", description = "channelId", required = true)
    @Parameter(name = "transferToken", description = "transferToken", required = true)
    public StreamingResponseBody downloadWithTransferToken(@RequestParam("channelId") String channelId,
                                                           @RequestParam("transferToken") String transferToken,
                                                           HttpServletResponse response) {
        return terminalSftpService.downloadWithTransferToken(channelId, transferToken, response);
    }

}

