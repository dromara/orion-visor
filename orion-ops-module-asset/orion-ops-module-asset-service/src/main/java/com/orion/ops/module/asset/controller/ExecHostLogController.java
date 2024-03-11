package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecHostLogOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecHostLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.ExecHostLogVO;
import com.orion.ops.module.asset.service.ExecHostLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 批量执行主机日志 api
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Tag(name = "asset - 批量执行主机日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec-host-log")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecHostLogController {

    @Resource
    private ExecHostLogService execHostLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "查询全部批量执行主机日志")
    @PreAuthorize("@ss.hasPermission('asset:exec-host-log:query')")
    public List<ExecHostLogVO> getExecHostLogList(@Validated @RequestBody ExecHostLogQueryRequest request) {
        return execHostLogService.getExecHostLogList(request);
    }

    @OperatorLog(ExecHostLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除批量执行主机日志")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-host-log:delete')")
    public Integer deleteExecHostLog(@RequestParam("id") Long id) {
        return execHostLogService.deleteExecHostLogById(id);
    }

    @OperatorLog(ExecHostLogOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除批量执行主机日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-host-log:delete')")
    public Integer batchDeleteExecHostLog(@RequestParam("idList") List<Long> idList) {
        return execHostLogService.deleteExecHostLogByIdList(idList);
    }

}

