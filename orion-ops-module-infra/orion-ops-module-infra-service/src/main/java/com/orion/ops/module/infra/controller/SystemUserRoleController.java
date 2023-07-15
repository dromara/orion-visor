package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.module.infra.service.*;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.entity.request.*;
import com.orion.ops.module.infra.convert.*;
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
 * 用户角色关联 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Tag(name = "infra - 用户角色关联服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-user-role")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class SystemUserRoleController {

    @Resource
    private SystemUserRoleService systemUserRoleService;

    @PostMapping("/create")
    @Operation(summary = "创建用户角色关联")
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:create')")
    public Long createSystemUserRole(@Validated @RequestBody SystemUserRoleCreateRequest request) {
        return systemUserRoleService.createSystemUserRole(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新用户角色关联")
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:update')")
    public Integer updateSystemUserRole(@Validated @RequestBody SystemUserRoleUpdateRequest request) {
        return systemUserRoleService.updateSystemUserRole(request);
    }

    @GetMapping("/get")
    @Operation(summary = "通过 id 查询用户角色关联")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:query')")
    public SystemUserRoleVO getSystemUserRole(@RequestParam("id") Long id) {
        return systemUserRoleService.getSystemUserRole(id);
    }

    @GetMapping("/list")
    @Operation(summary = "通过 id 批量查询用户角色关联")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:query')")
    public List<SystemUserRoleVO> getSystemUserRoleList(@RequestParam("idList") List<Long> idList) {
        return systemUserRoleService.getSystemUserRoleList(idList);
    }

    @PostMapping("/query")
    @Operation(summary = "分页查询用户角色关联")
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:query')")
    public DataGrid<SystemUserRoleVO> getSystemUserRolePage(@Validated @RequestBody SystemUserRoleQueryRequest request) {
        return systemUserRoleService.getSystemUserRolePage(request);
    }

    @PutMapping("/delete")
    @Operation(summary = "通过 id 删除用户角色关联")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:delete')")
    public Integer deleteSystemUserRole(@RequestParam("id") Long id) {
        return systemUserRoleService.deleteSystemUserRole(id);
    }

    @PutMapping("/delete-batch")
    @Operation(summary = "通过 id 批量删除用户角色关联")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user-role:delete')")
    public Integer batchDeleteSystemUserRole(@RequestParam("idList") List<Long> idList) {
        return systemUserRoleService.batchDeleteSystemUserRole(idList);
    }

}

