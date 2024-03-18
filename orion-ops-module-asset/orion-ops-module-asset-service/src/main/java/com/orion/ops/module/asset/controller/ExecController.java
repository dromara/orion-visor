package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecInterruptRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.ops.module.asset.entity.request.exec.ReExecCommandRequest;
import com.orion.ops.module.asset.entity.vo.ExecCommandVO;
import com.orion.ops.module.asset.service.ExecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 批量执行
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:44
 */
@Tag(name = "asset - 批量执行服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecController {

    @Resource
    private ExecService execService;

    @OperatorLog(ExecOperatorType.EXEC_COMMAND)
    @PostMapping("/exec-command")
    @Operation(summary = "批量执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec:exec-command')")
    public ExecCommandVO execCommand(@Validated @RequestBody ExecCommandRequest request) {
        return execService.execCommand(request);
    }

    @OperatorLog(ExecOperatorType.EXEC_COMMAND)
    @PostMapping("/re-exec-command")
    @Operation(summary = "重新执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec:exec-command')")
    public ExecCommandVO reExecCommand(@Validated @RequestBody ReExecCommandRequest request) {
        return execService.reExecCommand(request.getLogId());
    }

    @OperatorLog(ExecOperatorType.INTERRUPT_EXEC)
    @PutMapping("/interrupt")
    @Operation(summary = "中断执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec:interrupt-exec')")
    public HttpWrapper<?> interruptExec(@RequestBody ExecInterruptRequest request) {
        Long logId = Valid.notNull(request.getLogId());
        execService.interruptExec(logId);
        return HttpWrapper.ok();
    }

    @OperatorLog(ExecOperatorType.INTERRUPT_HOST)
    @PutMapping("/interrupt-host")
    @Operation(summary = "中断执行主机命令")
    @PreAuthorize("@ss.hasPermission('asset:exec:interrupt-exec')")
    public HttpWrapper<?> interruptHostExec(@RequestBody ExecInterruptRequest request) {
        Long hostLogId = Valid.notNull(request.getHostLogId());
        execService.interruptHostExec(hostLogId);
        return HttpWrapper.ok();
    }

    @PostMapping("/tail-log")
    @Operation(summary = "查看批量执行日志")
    @PreAuthorize("@ss.hasAnyPermission('asset:exec:exec-command', 'asset:exec-log:query')")
    public String getExecLogTailToken(@Validated @RequestBody ExecLogTailRequest request) {
        return execService.getExecLogTailToken(request);
    }

    @OperatorLog(ExecOperatorType.DOWNLOAD_HOST_LOG)
    @GetMapping("/download-log")
    @Operation(summary = "下载执行日志文件")
    @PreAuthorize("@ss.hasAnyPermission('asset:exec:exec-command', 'asset:exec-log:query')")
    public void downloadLogFile(@RequestParam("id") Long id, HttpServletResponse response) {
        execService.downloadLogFile(id, response);
    }

}
