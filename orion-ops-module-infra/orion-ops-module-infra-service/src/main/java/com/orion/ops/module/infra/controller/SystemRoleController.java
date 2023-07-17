package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.role.SystemRoleCreateRequest;
import com.orion.ops.module.infra.entity.request.role.SystemRoleQueryRequest;
import com.orion.ops.module.infra.entity.request.role.SystemRoleStatusRequest;
import com.orion.ops.module.infra.entity.request.role.SystemRoleUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemRoleVO;
import com.orion.ops.module.infra.service.SystemRoleService;
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
 * 角色 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 03:03
 */
@Tag(name = "infra - 角色服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-role")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class SystemRoleController {

    @Resource
    private SystemRoleService systemRoleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:create')")
    public Long createSystemRole(@Validated @RequestBody SystemRoleCreateRequest request) {
        return systemRoleService.createSystemRole(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:update')")
    public Integer updateSystemRole(@Validated @RequestBody SystemRoleUpdateRequest request) {
        return systemRoleService.updateSystemRole(request);
    }

    @PutMapping("/update-status")
    @Operation(summary = "通过 id 更新角色状态")
    @PreAuthorize("@ss.hasPermission('infra:system-role:update-status')")
    public Integer updateRoleStatus(@Validated @RequestBody SystemRoleStatusRequest request) {
        return systemRoleService.updateRoleStatus(request);
    }

    @GetMapping("/get")
    @Operation(summary = "通过 id 查询角色")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public SystemRoleVO getSystemRole(@RequestParam("id") Long id) {
        return systemRoleService.getSystemRole(id);
    }

    @GetMapping("/list")
    @Operation(summary = "通过 id 批量查询角色")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public List<SystemRoleVO> getSystemRoleList(@RequestParam("idList") List<Long> idList) {
        return systemRoleService.getSystemRoleList(idList);
    }

    @PostMapping("/query")
    @Operation(summary = "分页查询角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public DataGrid<SystemRoleVO> getSystemRolePage(@Validated @RequestBody SystemRoleQueryRequest request) {
        return systemRoleService.getSystemRolePage(request);
    }

    @PutMapping("/delete")
    @Operation(summary = "通过 id 删除角色")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:delete')")
    public Integer deleteSystemRole(@RequestParam("id") Long id) {
        return systemRoleService.deleteSystemRole(id);
    }

}

