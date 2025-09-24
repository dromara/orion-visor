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
package org.dromara.visor.module.exec.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.exec.define.operator.ExecJobLogOperatorType;
import org.dromara.visor.module.exec.entity.request.exec.ExecInterruptRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecLogClearRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecLogQueryRequest;
import org.dromara.visor.module.exec.entity.vo.ExecHostLogVO;
import org.dromara.visor.module.exec.entity.vo.ExecLogStatusVO;
import org.dromara.visor.module.exec.entity.vo.ExecLogVO;
import org.dromara.visor.module.exec.enums.ExecSourceEnum;
import org.dromara.visor.module.exec.service.ExecHostLogService;
import org.dromara.visor.module.exec.service.ExecLogService;
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
@Tag(name = "exec - 计划任务日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/exec/exec-job-log")
public class ExecJobLogController {

    private static final String SOURCE = ExecSourceEnum.JOB.name();

    @Resource
    private ExecLogService execLogService;

    @Resource
    private ExecHostLogService execHostLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询计划任务日志")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public DataGrid<ExecLogVO> getExecJobLogPage(@Validated(Page.class) @RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.getExecLogPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询计划任务日志")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public ExecLogVO getExecJobLog(@RequestParam("id") Long id) {
        return execLogService.getExecLog(id, SOURCE);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-host")
    @Operation(summary = "查询执行主机日志")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public ExecHostLogVO getExecJobHostLog(@RequestParam("id") Long id) {
        return execHostLogService.getExecHostLog(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/host-list")
    @Operation(summary = "查询全部执行主机日志")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public List<ExecHostLogVO> getExecJobHostLogList(@RequestParam("logId") Long logId) {
        return execHostLogService.getExecHostLogList(logId);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/status")
    @Operation(summary = "查询命令执行状态")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public ExecLogStatusVO getExecJobLogStatus(@RequestParam("idList") List<Long> idList) {
        return execLogService.getExecLogStatus(idList, SOURCE);
    }

    @PostMapping("/count")
    @Operation(summary = "查询计划任务日志数量")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public Long getExecJobLogCount(@Validated @RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.queryExecLogCount(request);
    }

    @OperatorLog(ExecJobLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除计划任务日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:delete')")
    public Integer deleteExecJobLog(@RequestParam("id") Long id) {
        return execLogService.deleteExecLogById(id, SOURCE);
    }

    @OperatorLog(ExecJobLogOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "删除计划任务日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:delete')")
    public Integer batchDeleteExecJobLog(@RequestParam("idList") List<Long> idList) {
        return execLogService.deleteExecLogByIdList(idList, SOURCE);
    }

    @OperatorLog(ExecJobLogOperatorType.DELETE_HOST)
    @DeleteMapping("/delete-host")
    @Operation(summary = "删除执行主机日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:delete')")
    public Integer deleteExecJobHostLog(@RequestParam("id") Long id) {
        return execHostLogService.deleteExecHostLogById(id);
    }

    @OperatorLog(ExecJobLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清理计划任务日志")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:management:clear')")
    public Integer clearExecJobLog(@Validated @RequestBody ExecLogClearRequest request) {
        request.setSource(SOURCE);
        return execLogService.clearExecLog(request);
    }

    @GetMapping("/tail")
    @Operation(summary = "查看计划任务日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public String getExecJobLogTailToken(@RequestParam("id") Long id) {
        return execLogService.getExecLogTailToken(id);
    }

    @OperatorLog(ExecJobLogOperatorType.DOWNLOAD)
    @GetMapping("/download")
    @Operation(summary = "下载计划任务日志")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:query')")
    public void downloadExecJobLogFile(@RequestParam("id") Long id, HttpServletResponse response) {
        execLogService.downloadLogFile(id, SOURCE, response);
    }

    @OperatorLog(ExecJobLogOperatorType.INTERRUPT)
    @PutMapping("/interrupt")
    @Operation(summary = "中断计划任务命令")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:interrupt')")
    public Boolean interruptExecCommand(@RequestBody ExecInterruptRequest request) {
        Long logId = Valid.notNull(request.getLogId());
        execLogService.interruptExec(logId, SOURCE);
        return true;
    }

    @OperatorLog(ExecJobLogOperatorType.INTERRUPT_HOST)
    @PutMapping("/interrupt-host")
    @Operation(summary = "中断计划任务主机命令")
    @PreAuthorize("@ss.hasPermission('exec:exec-job-log:interrupt')")
    public Boolean interruptHostExecCommand(@RequestBody ExecInterruptRequest request) {
        Long hostLogId = Valid.notNull(request.getHostLogId());
        execLogService.interruptHostExec(hostLogId, SOURCE);
        return true;
    }

}
