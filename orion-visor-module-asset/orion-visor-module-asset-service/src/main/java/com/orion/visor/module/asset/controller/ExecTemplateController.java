package com.orion.visor.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.ExecTemplateOperatorType;
import com.orion.visor.module.asset.entity.request.exec.ExecTemplateCreateRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecTemplateQueryRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecTemplateUpdateRequest;
import com.orion.visor.module.asset.entity.vo.ExecTemplateVO;
import com.orion.visor.module.asset.service.ExecTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class ExecTemplateController {

    @Resource
    private ExecTemplateService execTemplateService;

    @OperatorLog(ExecTemplateOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建执行模板")
    @PreAuthorize("@ss.hasPermission('asset:exec-template:create')")
    public Long createExecTemplate(@Validated @RequestBody ExecTemplateCreateRequest request) {
        return execTemplateService.createExecTemplate(request);
    }

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

    @OperatorLog(ExecTemplateOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除执行模板")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:exec-template:delete')")
    public Integer deleteExecTemplate(@RequestParam("id") Long id) {
        return execTemplateService.deleteExecTemplateById(id);
    }

}

