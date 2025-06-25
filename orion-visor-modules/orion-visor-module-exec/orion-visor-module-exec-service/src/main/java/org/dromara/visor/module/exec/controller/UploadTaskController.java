/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.exec.controller;

import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.exec.define.operator.UploadTaskOperatorType;
import org.dromara.visor.module.exec.entity.request.upload.UploadTaskClearRequest;
import org.dromara.visor.module.exec.entity.request.upload.UploadTaskCreateRequest;
import org.dromara.visor.module.exec.entity.request.upload.UploadTaskQueryRequest;
import org.dromara.visor.module.exec.entity.request.upload.UploadTaskRequest;
import org.dromara.visor.module.exec.entity.vo.UploadTaskCreateVO;
import org.dromara.visor.module.exec.entity.vo.UploadTaskStatusVO;
import org.dromara.visor.module.exec.entity.vo.UploadTaskVO;
import org.dromara.visor.module.exec.service.UploadTaskService;
import cn.orionsec.kit.lang.define.wrapper.DataGrid;
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
@Tag(name = "exec - 上传任务服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/exec/upload-task")
public class UploadTaskController {

    @Resource
    private UploadTaskService uploadTaskService;

    @OperatorLog(UploadTaskOperatorType.UPLOAD)
    @PostMapping("/create")
    @Operation(summary = "创建上传任务")
    @PreAuthorize("@ss.hasPermission('exec:upload-task:upload')")
    public UploadTaskCreateVO createUploadTask(@Validated @RequestBody UploadTaskCreateRequest request) {
        return uploadTaskService.createUploadTask(request);
    }

    @PostMapping("/start")
    @Operation(summary = "开始上传")
    @PreAuthorize("@ss.hasPermission('exec:upload-task:upload')")
    public Boolean startUploadTask(@Validated @RequestBody UploadTaskRequest request) {
        uploadTaskService.startUploadTask(request.getId());
        return true;
    }

    @OperatorLog(UploadTaskOperatorType.CANCEL)
    @PostMapping("/cancel")
    @Operation(summary = "取消上传")
    @PreAuthorize("@ss.hasPermission('exec:upload-task:upload')")
    public Boolean cancelUploadTask(@Validated @RequestBody UploadTaskRequest request) {
        uploadTaskService.cancelUploadTask(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询上传任务")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('exec:upload-task:query')")
    public UploadTaskVO getUploadTask(@RequestParam("id") Long id) {
        return uploadTaskService.getUploadTask(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询上传任务")
    @PreAuthorize("@ss.hasPermission('exec:upload-task:query')")
    public DataGrid<UploadTaskVO> getUploadTaskPage(@Validated(Page.class) @RequestBody UploadTaskQueryRequest request) {
        return uploadTaskService.getUploadTaskPage(request);
    }

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/status")
    @Operation(summary = "查询上传状态")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('exec:upload-task:query')")
    public List<UploadTaskStatusVO> getUploadTaskStatus(@RequestParam("idList") List<Long> idList, @RequestParam("queryFiles") Boolean queryFiles) {
        return uploadTaskService.getUploadTaskStatus(idList, queryFiles);
    }

    @PostMapping("/count")
    @Operation(summary = "查询上传任务数量")
    @PreAuthorize("@ss.hasPermission('exec:upload-task:query')")
    public Long getUploadTaskCount(@Validated @RequestBody UploadTaskQueryRequest request) {
        return uploadTaskService.getUploadTaskCount(request);
    }

    @OperatorLog(UploadTaskOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除上传任务")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('exec:upload-task:delete')")
    public Integer deleteUploadTask(@RequestParam("id") Long id) {
        return uploadTaskService.deleteUploadTaskById(id);
    }

    @OperatorLog(UploadTaskOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除上传任务")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('exec:upload-task:delete')")
    public Integer batchDeleteUploadTask(@RequestParam("idList") List<Long> idList) {
        return uploadTaskService.deleteUploadTaskByIdList(idList);
    }

    @OperatorLog(UploadTaskOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空上传任务")
    @PreAuthorize("@ss.hasPermission('exec:upload-task:management:clear')")
    public Integer clearUploadTask(@Validated @RequestBody UploadTaskClearRequest request) {
        return uploadTaskService.clearUploadTask(request);
    }

}

