package com.orion.visor.module.asset.controller;

import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.biz.operator.log.core.enums.ReturnType;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.ExecCommandOperatorType;
import com.orion.visor.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.visor.module.asset.entity.request.exec.ReExecCommandRequest;
import com.orion.visor.module.asset.entity.vo.ExecLogVO;
import com.orion.visor.module.asset.service.ExecCommandService;
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
@RequestMapping("/asset/exec-command")
public class ExecCommandController {

    @Resource
    private ExecCommandService execCommandService;

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

}
