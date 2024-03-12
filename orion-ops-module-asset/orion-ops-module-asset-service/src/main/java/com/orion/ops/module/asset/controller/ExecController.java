package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecInterruptRequest;
import com.orion.ops.module.asset.entity.vo.ExecVO;
import com.orion.ops.module.asset.service.ExecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/asset/exec")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecController {

    @Resource
    private ExecService execService;

    @OperatorLog(ExecOperatorType.EXEC_COMMAND)
    @PostMapping("/exec-command")
    @Operation(summary = "批量执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec:exec-command')")
    public ExecVO execCommand(@RequestBody ExecCommandRequest request) {
        return execService.execCommand(request);
    }

    @OperatorLog(ExecOperatorType.INTERRUPT_COMMAND)
    @PostMapping("/interrupt-command")
    @Operation(summary = "中断执行命令")
    @PreAuthorize("@ss.hasPermission('asset:exec:interrupt-command')")
    public HttpWrapper<?> interruptCommand(@RequestBody ExecInterruptRequest request) {
        execService.interruptCommand(request);
        return HttpWrapper.ok();
    }

    // log

}
