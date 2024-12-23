/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.common.validator.group.Page;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.define.operator.ExecTemplateOperatorType;
import org.dromara.visor.module.asset.entity.request.exec.ExecTemplateCreateRequest;
import org.dromara.visor.module.asset.entity.request.exec.ExecTemplateQueryRequest;
import org.dromara.visor.module.asset.entity.request.exec.ExecTemplateUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.ExecTemplateVO;
import org.dromara.visor.module.asset.service.ExecTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 执行模板 api
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Tag(name = "asset - 执行模板服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/exec-template")
public class ExecTemplateController {

    @Resource
    private ExecTemplateService execTemplateService;

    @DemoDisableApi
    @OperatorLog(ExecTemplateOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建执行模板")
    @PreAuthorize("@ss.hasPermission('asset:exec-template:create')")
    public Long createExecTemplate(@Validated @RequestBody ExecTemplateCreateRequest request) {
        return execTemplateService.createExecTemplate(request);
    }

    @DemoDisableApi
    @OperatorLog(ExecTemplateOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新执行模板")
    @PreAuthorize("@ss.hasPermission('asset:exec-template:update')")
    public Integer updateExecTemplate(@Validated @RequestBody ExecTemplateUpdateRequest request) {
        return execTemplateService.updateExecTemplateById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询执行模板")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-template:query')")
    public ExecTemplateVO getExecTemplate(@RequestParam("id") Long id) {
        return execTemplateService.getExecTemplateById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-with-authorized")
    @Operation(summary = "查询执行模板 (查询认证的主机)")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-template:query')")
    public ExecTemplateVO getExecTemplateWithAuthorized(@RequestParam("id") Long id) {
        return execTemplateService.getExecTemplateWithAuthorized(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询执行模板")
    @PreAuthorize("@ss.hasPermission('asset:exec-template:query')")
    public DataGrid<ExecTemplateVO> getExecTemplatePage(@Validated(Page.class) @RequestBody ExecTemplateQueryRequest request) {
        return execTemplateService.getExecTemplatePage(request);
    }

    @DemoDisableApi
    @OperatorLog(ExecTemplateOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除执行模板")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-template:delete')")
    public Integer deleteExecTemplate(@RequestParam("id") Long id) {
        return execTemplateService.deleteExecTemplateById(id);
    }

    @DemoDisableApi
    @OperatorLog(ExecTemplateOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除执行模板")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-template:delete')")
    public Integer batchDeleteExecTemplate(@RequestParam("idList") List<Long> idList) {
        return execTemplateService.deleteExecTemplateByIdList(idList);
    }

}

