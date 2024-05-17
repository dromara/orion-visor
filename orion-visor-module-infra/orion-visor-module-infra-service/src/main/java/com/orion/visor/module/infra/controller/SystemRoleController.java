package com.orion.visor.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.define.operator.SystemRoleOperatorType;
import com.orion.visor.module.infra.entity.request.menu.SystemRoleGrantMenuRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleCreateRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleQueryRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleStatusRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleUpdateRequest;
import com.orion.visor.module.infra.entity.vo.SystemRoleVO;
import com.orion.visor.module.infra.service.SystemRoleMenuService;
import com.orion.visor.module.infra.service.SystemRoleService;
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

    @Resource
    private SystemRoleMenuService systemRoleMenuService;

    @OperatorLog(SystemRoleOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:create')")
    public Long createSystemRole(@Validated @RequestBody SystemRoleCreateRequest request) {
        return systemRoleService.createSystemRole(request);
    }

    @OperatorLog(SystemRoleOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:update')")
    public Integer updateSystemRole(@Validated @RequestBody SystemRoleUpdateRequest request) {
        return systemRoleService.updateSystemRoleById(request);
    }

    @OperatorLog(SystemRoleOperatorType.UPDATE_STATUS)
    @PutMapping("/update-status")
    @Operation(summary = "通过 id 更新角色状态")
    @PreAuthorize("@ss.hasPermission('infra:system-role:update-status')")
    public Integer updateRoleStatus(@Validated @RequestBody SystemRoleStatusRequest request) {
        return systemRoleService.updateRoleStatus(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询角色")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public SystemRoleVO getSystemRole(@RequestParam("id") Long id) {
        return systemRoleService.getSystemRoleById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询所有角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public List<SystemRoleVO> getSystemRoleList() {
        return systemRoleService.getSystemRoleByIdList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public DataGrid<SystemRoleVO> getSystemRolePage(@Validated(Page.class) @RequestBody SystemRoleQueryRequest request) {
        return systemRoleService.getSystemRolePage(request);
    }

    @GetMapping("/get-menu-id")
    @Operation(summary = "获取角色菜单id")
    @Parameter(name = "roleId", description = "roleId", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:query')")
    public List<Long> getRoleMenuIdList(@RequestParam("roleId") Long roleId) {
        return systemRoleMenuService.getRoleMenuIdList(roleId);
    }

    @OperatorLog(SystemRoleOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除角色")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:delete')")
    public Integer deleteSystemRole(@RequestParam("id") Long id) {
        return systemRoleService.deleteSystemRoleById(id);
    }

    @OperatorLog(SystemRoleOperatorType.GRANT_MENU)
    @PutMapping("/grant-menu")
    @Operation(summary = "分配角色菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-role:grant-menu')")
    public Integer grantRoleMenu(@RequestBody SystemRoleGrantMenuRequest request) {
        return systemRoleMenuService.grantRoleMenu(request);
    }

}

