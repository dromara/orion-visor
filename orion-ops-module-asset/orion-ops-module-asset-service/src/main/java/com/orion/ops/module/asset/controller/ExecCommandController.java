package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.biz.operator.log.core.enums.ReturnType;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecCommandOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecInterruptRequest;
import com.orion.ops.module.asset.entity.request.exec.ReExecCommandRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;
import com.orion.ops.module.asset.enums.ExecSourceEnum;
import com.orion.ops.module.asset.service.ExecCommandService;
import com.orion.ops.module.asset.service.ExecLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
@RequestMapping("/asset/exec-command")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecCommandController {

    private static final String SOURCE = ExecSourceEnum.BATCH.name();

    @Resource
    private ExecCommandService execCommandService;

    @Resource
    private ExecLogService execLogService;

    @OperatorLog(value = ExecCommandOperatorType.EXEC, ret = ReturnType.IGNORE)
    @PostMapping("/exec")
    @Operation(summary = "批量执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-command:exec')")
    public ExecLogVO execCommand(@Validated @RequestBody ExecCommandRequest request) {
        return execCommandService.execCommand(request);
    }

    @OperatorLog(value = ExecCommandOperatorType.EXEC, ret = ReturnType.IGNORE)
    @PostMapping("/re-exec")
    @Operation(summary = "重新执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-command:exec')")
    public ExecLogVO reExecCommand(@Validated @RequestBody ReExecCommandRequest request) {
        return execCommandService.reExecCommand(request.getLogId());
    }

    @OperatorLog(ExecCommandOperatorType.INTERRUPT_EXEC)
    @PutMapping("/interrupt")
    @Operation(summary = "中断执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-command:interrupt')")
    public HttpWrapper<?> interruptExecCommand(@RequestBody ExecInterruptRequest request) {
        Long logId = Valid.notNull(request.getLogId());
        execLogService.interruptExec(logId, SOURCE);
        return HttpWrapper.ok();
    }

    @OperatorLog(ExecCommandOperatorType.INTERRUPT_HOST)
    @PutMapping("/interrupt-host")
    @Operation(summary = "中断执行主机命令")
    @PreAuthorize("@ss.hasPermission('asset:exec-command:interrupt')")
    public HttpWrapper<?> interruptHostExecCommand(@RequestBody ExecInterruptRequest request) {
        Long hostLogId = Valid.notNull(request.getHostLogId());
        execLogService.interruptHostExec(hostLogId, SOURCE);
        return HttpWrapper.ok();
    }

}
