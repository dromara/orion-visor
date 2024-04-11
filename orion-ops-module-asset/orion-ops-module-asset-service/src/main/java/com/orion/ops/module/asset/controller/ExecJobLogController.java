package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecJobLogOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.ops.module.asset.entity.vo.ExecHostLogVO;
import com.orion.ops.module.asset.entity.vo.ExecLogStatusVO;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;
import com.orion.ops.module.asset.enums.ExecSourceEnum;
import com.orion.ops.module.asset.service.ExecHostLogService;
import com.orion.ops.module.asset.service.ExecLogService;
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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
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

    @PostMapping("/query-count")
    @Operation(summary = "查询计划任务日志数量")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:management:clear')")
    public Long getExecJobLogCount(@RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.queryExecLogCount(request);
    }

    @OperatorLog(ExecJobLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空计划任务日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-log:management:clear')")
    public Integer clearExecJobLog(@RequestBody ExecLogQueryRequest request) {
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

}

