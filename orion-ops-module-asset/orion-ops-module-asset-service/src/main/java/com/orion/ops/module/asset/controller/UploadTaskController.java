package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.UploadTaskOperatorType;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskCreateRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskQueryRequest;
import com.orion.ops.module.asset.entity.request.upload.UploadTaskRequest;
import com.orion.ops.module.asset.entity.vo.UploadTaskCreateVO;
import com.orion.ops.module.asset.entity.vo.UploadTaskVO;
import com.orion.ops.module.asset.service.UploadTaskService;
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
 * 上传任务 api
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Tag(name = "asset - 上传任务服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/upload-task")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class UploadTaskController {

    // TODO 前端日志 测试删除慢吗

    @Resource
    private UploadTaskService uploadTaskService;

    @OperatorLog(UploadTaskOperatorType.UPLOAD)
    @PostMapping("/create")
    @Operation(summary = "创建上传任务")
    @PreAuthorize("@ss.hasPermission('asset:upload-task:upload')")
    public UploadTaskCreateVO createUploadTask(@Validated @RequestBody UploadTaskCreateRequest request) {
        return uploadTaskService.createUploadTask(request);
    }

    @PostMapping("/start")
    @Operation(summary = "开始上传")
    @PreAuthorize("@ss.hasPermission('asset:upload-task:upload')")
    public Boolean startUploadTask(@Validated @RequestBody UploadTaskRequest request) {
        uploadTaskService.startUploadTask(request.getId());
        return true;
    }

    @OperatorLog(UploadTaskOperatorType.CANCEL)
    @PostMapping("/cancel")
    @Operation(summary = "取消上传")
    @PreAuthorize("@ss.hasPermission('asset:upload-task:upload')")
    public Boolean cancelUploadTask(@Validated @RequestBody UploadTaskRequest request) {
        uploadTaskService.cancelUploadTask(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询上传任务")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:upload-task:query')")
    public UploadTaskVO getUploadTask(@RequestParam("id") Long id) {
        return uploadTaskService.getUploadTask(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询上传任务")
    @PreAuthorize("@ss.hasPermission('asset:upload-task:query')")
    public DataGrid<UploadTaskVO> getUploadTaskPage(@Validated(Page.class) @RequestBody UploadTaskQueryRequest request) {
        return uploadTaskService.getUploadTaskPage(request);
    }

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/status")
    @Operation(summary = "查询上传状态")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:upload-task:query')")
    public List<UploadTaskVO> getUploadTaskStatus(@RequestParam("idList") List<Long> idList, @RequestParam("queryFiles") Boolean queryFiles) {
        return uploadTaskService.getUploadTaskStatus(idList, queryFiles);
    }

    @OperatorLog(UploadTaskOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除上传任务")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:upload-task:delete')")
    public Integer deleteUploadTask(@RequestParam("id") Long id) {
        return uploadTaskService.deleteUploadTaskById(id);
    }

    @OperatorLog(UploadTaskOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除上传任务")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:upload-task:delete')")
    public Integer batchDeleteUploadTask(@RequestParam("idList") List<Long> idList) {
        return uploadTaskService.deleteUploadTaskByIdList(idList);
    }

    @PostMapping("/query-count")
    @Operation(summary = "查询上传任务数量")
    @PreAuthorize("@ss.hasPermission('asset:upload-task:management:clear')")
    public Long getUploadTaskCount(@RequestBody UploadTaskQueryRequest request) {
        return uploadTaskService.getUploadTaskCount(request);
    }

    @OperatorLog(UploadTaskOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空上传任务")
    @PreAuthorize("@ss.hasPermission('asset:upload-task:management:clear')")
    public Integer clearUploadTask(@RequestBody UploadTaskQueryRequest request) {
        return uploadTaskService.clearUploadTask(request);
    }

}

