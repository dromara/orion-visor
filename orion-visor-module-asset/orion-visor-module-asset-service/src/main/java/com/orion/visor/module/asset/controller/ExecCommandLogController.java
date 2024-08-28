package com.orion.visor.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.common.validator.group.Clear;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.ExecCommandLogOperatorType;
import com.orion.visor.module.asset.entity.request.exec.ExecInterruptRequest;
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
 * 批量执行日志 api
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Tag(name = "asset - 批量执行日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec-command-log")
public class ExecCommandLogController {

    private static final String SOURCE = ExecSourceEnum.BATCH.name();

    @Resource
    private ExecLogService execLogService;

    @Resource
    private ExecHostLogService execHostLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询批量执行日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:query')")
    public DataGrid<ExecLogVO> getExecCommandLogPage(@Validated(Page.class) @RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.getExecLogPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询批量执行日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:query')")
    public ExecLogVO getExecCommandLog(@RequestParam("id") Long id) {
        return execLogService.getExecLog(id, SOURCE);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/host-list")
    @Operation(summary = "查询全部执行主机日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:query')")
    public List<ExecHostLogVO> getExecCommandHostLogList(@RequestParam("logId") Long logId) {
        return execHostLogService.getExecHostLogList(logId);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/status")
    @Operation(summary = "查询命令执行状态")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:query')")
    public ExecLogStatusVO getExecCommandLogStatus(@RequestParam("idList") List<Long> idList) {
        return execLogService.getExecLogStatus(idList, SOURCE);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/history")
    @Operation(summary = "查询执行历史")
    @PreAuthorize("@ss.hasAnyPermission('asset:exec-command-log:query', 'asset:exec-command:exec')")
    public List<ExecLogVO> getExecCommandLogHistory(@Validated(Page.class) ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        request.setUserId(SecurityUtils.getLoginUserId());
        return execLogService.getExecHistory(request);
    }

    @OperatorLog(ExecCommandLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除批量执行日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:delete')")
    public Integer deleteExecCommandLog(@RequestParam("id") Long id) {
        return execLogService.deleteExecLogById(id, SOURCE);
    }

    @OperatorLog(ExecCommandLogOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "删除批量执行日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:delete')")
    public Integer batchDeleteExecCommandLog(@RequestParam("idList") List<Long> idList) {
        return execLogService.deleteExecLogByIdList(idList, SOURCE);
    }

    @OperatorLog(ExecCommandLogOperatorType.DELETE_HOST)
    @DeleteMapping("/delete-host")
    @Operation(summary = "删除执行主机日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:delete')")
    public Integer deleteExecCommandHostLog(@RequestParam("id") Long id) {
        return execHostLogService.deleteExecHostLogById(id);
    }

    @PostMapping("/query-count")
    @Operation(summary = "查询批量执行日志数量")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:management:clear')")
    public Long getExecCommandLogCount(@RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.queryExecLogCount(request);
    }

    @OperatorLog(ExecCommandLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空批量执行日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:management:clear')")
    public Integer clearExecCommandLog(@Validated(Clear.class) @RequestBody ExecLogQueryRequest request) {
        request.setSource(SOURCE);
        return execLogService.clearExecLog(request);
    }

    @PostMapping("/tail")
    @Operation(summary = "查看批量执行日志")
    @PreAuthorize("@ss.hasAnyPermission('asset:exec-command-log:query', 'asset:exec-command:exec')")
    public String getExecCommandLogTailToken(@Validated @RequestBody ExecLogTailRequest request) {
        request.setSource(SOURCE);
        return execLogService.getExecLogTailToken(request);
    }

    @OperatorLog(ExecCommandLogOperatorType.DOWNLOAD)
    @GetMapping("/download")
    @Operation(summary = "下载批量执行日志")
    @PreAuthorize("@ss.hasAnyPermission('asset:exec-command-log:query', 'asset:exec-command:exec')")
    public void downloadExecCommandLogFile(@RequestParam("id") Long id, HttpServletResponse response) {
        execLogService.downloadLogFile(id, SOURCE, response);
    }

    @OperatorLog(value = ExecCommandLogOperatorType.INTERRUPT)
    @PutMapping("/interrupt")
    @Operation(summary = "中断批量执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:interrupt')")
    public Boolean interruptExecCommand(@RequestBody ExecInterruptRequest request) {
        Long logId = Valid.notNull(request.getLogId());
        execLogService.interruptExec(logId, SOURCE);
        return true;
    }

    @OperatorLog(value = ExecCommandLogOperatorType.INTERRUPT_HOST)
    @PutMapping("/interrupt-host")
    @Operation(summary = "中断批量执行主机命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-command-log:interrupt')")
    public Boolean interruptHostExecCommand(@RequestBody ExecInterruptRequest request) {
        Long hostLogId = Valid.notNull(request.getHostLogId());
        execLogService.interruptHostExec(hostLogId, SOURCE);
        return true;
    }

}
