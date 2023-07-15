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
 * 角色菜单关联 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Tag(name = "infra - 角色菜单关联服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-role-menu")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class SystemRoleMenuController {

    @Resource
    private SystemRoleMenuService systemRoleMenuService;

    @PostMapping("/create")
    @Operation(summary = "创建角色菜单关联")
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:create')")
    public Long createSystemRoleMenu(@Validated @RequestBody SystemRoleMenuCreateRequest request) {
        return systemRoleMenuService.createSystemRoleMenu(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新角色菜单关联")
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:update')")
    public Integer updateSystemRoleMenu(@Validated @RequestBody SystemRoleMenuUpdateRequest request) {
        return systemRoleMenuService.updateSystemRoleMenu(request);
    }

    @GetMapping("/get")
    @Operation(summary = "通过 id 查询角色菜单关联")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:query')")
    public SystemRoleMenuVO getSystemRoleMenu(@RequestParam("id") Long id) {
        return systemRoleMenuService.getSystemRoleMenu(id);
    }

    @GetMapping("/list")
    @Operation(summary = "通过 id 批量查询角色菜单关联")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:query')")
    public List<SystemRoleMenuVO> getSystemRoleMenuList(@RequestParam("idList") List<Long> idList) {
        return systemRoleMenuService.getSystemRoleMenuList(idList);
    }

    @PostMapping("/query")
    @Operation(summary = "分页查询角色菜单关联")
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:query')")
    public DataGrid<SystemRoleMenuVO> getSystemRoleMenuPage(@Validated @RequestBody SystemRoleMenuQueryRequest request) {
        return systemRoleMenuService.getSystemRoleMenuPage(request);
    }

    @PutMapping("/delete")
    @Operation(summary = "通过 id 删除角色菜单关联")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:delete')")
    public Integer deleteSystemRoleMenu(@RequestParam("id") Long id) {
        return systemRoleMenuService.deleteSystemRoleMenu(id);
    }

    @PutMapping("/delete-batch")
    @Operation(summary = "通过 id 批量删除角色菜单关联")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role-menu:delete')")
    public Integer batchDeleteSystemRoleMenu(@RequestParam("idList") List<Long> idList) {
        return systemRoleMenuService.batchDeleteSystemRoleMenu(idList);
    }

}

