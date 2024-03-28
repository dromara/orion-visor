package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecJobHostOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobHostVO;
import com.orion.ops.module.asset.service.ExecJobHostService;
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
 * 计划执行任务主机 api
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Tag(name = "asset - 计划执行任务主机服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec-job-host")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecJobHostController {

    @Resource
    private ExecJobHostService execJobHostService;

    @OperatorLog(ExecJobHostOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建计划执行任务主机")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:create')")
    public Long createExecJobHost(@Validated @RequestBody ExecJobHostCreateRequest request) {
        return execJobHostService.createExecJobHost(request);
    }

    @OperatorLog(ExecJobHostOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新计划执行任务主机")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:update')")
    public Integer updateExecJobHost(@Validated @RequestBody ExecJobHostUpdateRequest request) {
        return execJobHostService.updateExecJobHostById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询计划执行任务主机")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:query')")
    public ExecJobHostVO getExecJobHost(@RequestParam("id") Long id) {
        return execJobHostService.getExecJobHostById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/batch-get")
    @Operation(summary = "批量查询计划执行任务主机")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:query')")
    public List<ExecJobHostVO> getExecJobHostBatch(@RequestParam("idList") List<Long> idList) {
        return execJobHostService.getExecJobHostByIdList(idList);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "查询全部计划执行任务主机")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:query')")
    public List<ExecJobHostVO> getExecJobHostList(@Validated @RequestBody ExecJobHostQueryRequest request) {
        return execJobHostService.getExecJobHostList(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询计划执行任务主机")
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:query')")
    public DataGrid<ExecJobHostVO> getExecJobHostPage(@Validated(Page.class) @RequestBody ExecJobHostQueryRequest request) {
        return execJobHostService.getExecJobHostPage(request);
    }

    @OperatorLog(ExecJobHostOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除计划执行任务主机")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:delete')")
    public Integer deleteExecJobHost(@RequestParam("id") Long id) {
        return execJobHostService.deleteExecJobHostById(id);
    }

    @OperatorLog(ExecJobHostOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除计划执行任务主机")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job-host:delete')")
    public Integer batchDeleteExecJobHost(@RequestParam("idList") List<Long> idList) {
        return execJobHostService.deleteExecJobHostByIdList(idList);
    }

}

