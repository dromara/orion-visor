/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.ExecJobLogOperatorType;
import com.orion.visor.module.asset.entity.request.exec.ExecInterruptRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecLogClearRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.visor.module.asset.entity.vo.ExecHostLogVO;
import com.orion.visor.module.asset.entity.vo.ExecLogStatusVO;
import com.orion.visor.module.asset.entity.vo.ExecLogVO;
import com.orion.visor.module.asset.enums.ExecSourceEnum;
import com.orion.visor.module.asset.service.ExecHostLogService;
import com.orion.visor.module.asset.service.ExecLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 计划任务日志 api
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Tag(name = "asset - 计划任务日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec-job-log")
public class ExecJobLogController {

    private static final String SOURCE = ExecSourceEnum.JOB.name();

    @Resource
    private ExecLogService execLogService;

    @Resource
    private ExecHostLogService execHostLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询计划任务日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public DataGrid<ExecLogVO> getExecJobLogPage(@Validated(Page.class) @RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.getExecLogPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询计划任务日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public ExecLogVO getExecJobLog(@RequestParam("id") Long id) {
        return execLogService.getExecLog(id, SOURCE);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/host-list")
    @Operation(summary = "查询全部执行主机日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public List<ExecHostLogVO> getExecJobHostLogList(@RequestParam("logId") Long logId) {
        return execHostLogService.getExecHostLogList(logId);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/status")
    @Operation(summary = "查询命令执行状态")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public ExecLogStatusVO getExecJobLogStatus(@RequestParam("idList") List<Long> idList) {
        return execLogService.getExecLogStatus(idList, SOURCE);
    }

    @PostMapping("/count")
    @Operation(summary = "查询计划任务日志数量")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public Long getExecJobLogCount(@Validated @RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.queryExecLogCount(request);
    }

    @OperatorLog(ExecJobLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除计划任务日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:delete')")
    public Integer deleteExecJobLog(@RequestParam("id") Long id) {
        return execLogService.deleteExecLogById(id, SOURCE);
    }

    @OperatorLog(ExecJobLogOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "删除计划任务日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:delete')")
    public Integer batchDeleteExecJobLog(@RequestParam("idList") List<Long> idList) {
        return execLogService.deleteExecLogByIdList(idList, SOURCE);
    }

    @OperatorLog(ExecJobLogOperatorType.DELETE_HOST)
    @DeleteMapping("/delete-host")
    @Operation(summary = "删除执行主机日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:delete')")
    public Integer deleteExecJobHostLog(@RequestParam("id") Long id) {
        return execHostLogService.deleteExecHostLogById(id);
    }

    @OperatorLog(ExecJobLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空计划任务日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:management:clear')")
    public Integer clearExecJobLog(@Validated @RequestBody ExecLogClearRequest request) {
        request.setSource(SOURCE);
        return execLogService.clearExecLog(request);
    }

    @PostMapping("/tail")
    @Operation(summary = "查看计划任务日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public String getExecJobLogTailToken(@Validated @RequestBody ExecLogTailRequest request) {
        request.setSource(SOURCE);
        return execLogService.getExecLogTailToken(request);
    }

    @OperatorLog(ExecJobLogOperatorType.DOWNLOAD)
    @GetMapping("/download")
    @Operation(summary = "下载计划任务日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:query')")
    public void downloadExecJobLogFile(@RequestParam("id") Long id, HttpServletResponse response) {
        execLogService.downloadLogFile(id, SOURCE, response);
    }

    @OperatorLog(ExecJobLogOperatorType.INTERRUPT)
    @PutMapping("/interrupt")
    @Operation(summary = "中断计划任务命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:interrupt')")
    public Boolean interruptExecCommand(@RequestBody ExecInterruptRequest request) {
        Long logId = Valid.notNull(request.getLogId());
        execLogService.interruptExec(logId, SOURCE);
        return true;
    }

    @OperatorLog(ExecJobLogOperatorType.INTERRUPT_HOST)
    @PutMapping("/interrupt-host")
    @Operation(summary = "中断计划任务主机命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:interrupt')")
    public Boolean interruptHostExecCommand(@RequestBody ExecInterruptRequest request) {
        Long hostLogId = Valid.notNull(request.getHostLogId());
        execLogService.interruptHostExec(hostLogId, SOURCE);
        return true;
    }

}
