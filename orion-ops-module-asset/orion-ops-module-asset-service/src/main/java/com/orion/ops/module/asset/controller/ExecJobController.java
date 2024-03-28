package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.ExecJobOperatorType;
import com.orion.ops.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;
import com.orion.ops.module.asset.service.ExecJobService;
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
 * 计划执行任务 api
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Tag(name = "asset - 计划执行任务服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec-job")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecJobController {

    @Resource
    private ExecJobService execJobService;

    @OperatorLog(ExecJobOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建计划执行任务")
    @PreAuthorize("@ss.hasPermission('asset:exec-job:create')")
    public Long createExecJob(@Validated @RequestBody ExecJobCreateRequest request) {
        return execJobService.createExecJob(request);
    }

    @OperatorLog(ExecJobOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新计划执行任务")
    @PreAuthorize("@ss.hasPermission('asset:exec-job:update')")
    public Integer updateExecJob(@Validated @RequestBody ExecJobUpdateRequest request) {
        return execJobService.updateExecJobById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询计划执行任务")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job:query')")
    public ExecJobVO getExecJob(@RequestParam("id") Long id) {
        return execJobService.getExecJobById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/batch-get")
    @Operation(summary = "批量查询计划执行任务")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job:query')")
    public List<ExecJobVO> getExecJobBatch(@RequestParam("idList") List<Long> idList) {
        return execJobService.getExecJobByIdList(idList);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "查询全部计划执行任务")
    @PreAuthorize("@ss.hasPermission('asset:exec-job:query')")
    public List<ExecJobVO> getExecJobList(@Validated @RequestBody ExecJobQueryRequest request) {
        return execJobService.getExecJobList(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询计划执行任务")
    @PreAuthorize("@ss.hasPermission('asset:exec-job:query')")
    public DataGrid<ExecJobVO> getExecJobPage(@Validated(Page.class) @RequestBody ExecJobQueryRequest request) {
        return execJobService.getExecJobPage(request);
    }

    @OperatorLog(ExecJobOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除计划执行任务")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job:delete')")
    public Integer deleteExecJob(@RequestParam("id") Long id) {
        return execJobService.deleteExecJobById(id);
    }

    @OperatorLog(ExecJobOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除计划执行任务")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-job:delete')")
    public Integer batchDeleteExecJob(@RequestParam("idList") List<Long> idList) {
        return execJobService.deleteExecJobByIdList(idList);
    }

}

