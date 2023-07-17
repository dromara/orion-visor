package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.user.*;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import com.orion.ops.module.infra.service.SystemUserRoleService;
import com.orion.ops.module.infra.service.SystemUserService;
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
 * 用户 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Tag(name = "infra - 用户服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-user")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class SystemUserController {

    @Resource
    private SystemUserService systemUserService;

    @Resource
    private SystemUserRoleService systemUserRoleService;

    @PostMapping("/create")
    @Operation(summary = "创建用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:create')")
    public Long createSystemUser(@Validated @RequestBody SystemUserCreateRequest request) {
        return systemUserService.createSystemUser(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update')")
    public Integer updateSystemUser(@Validated @RequestBody SystemUserUpdateRequest request) {
        return systemUserService.updateSystemUser(request);
    }

    // TODO 修改头像

    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update-status')")
    public Integer updateUserStatus(@Validated @RequestBody SystemUserUpdateStatusRequest request) {
        return systemUserService.updateUserStatus(request);
    }

    @PutMapping("/update-role")
    @Operation(summary = "修改用户角色")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update-role')")
    public Integer updateUserRole(@Validated @RequestBody SystemUserUpdateRoleRequest request) {
        if (Lists.isEmpty(request.getRoles())) {
            // 删除用户角色
            return systemUserRoleService.deleteUserRoles(request);
        } else {
            // 更新用户橘色
            return systemUserRoleService.updateUserRoles(request);
        }
    }

    @PutMapping("/reset-password")
    @Operation(summary = "重置密码")
    @PreAuthorize("@ss.hasPermission('infra:system-user:reset-password')")
    public HttpWrapper<?> resetPassword(@Validated @RequestBody UserResetPasswordRequest request) {
        systemUserService.resetPassword(request);
        return HttpWrapper.ok();
    }

    @GetMapping("/get")
    @Operation(summary = "通过 id 查询用户")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public SystemUserVO getSystemUser(@RequestParam("id") Long id) {
        return systemUserService.getSystemUser(id);
    }

    @GetMapping("/list")
    @Operation(summary = "通过 id 批量查询用户")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public List<SystemUserVO> getSystemUserList(@RequestParam("idList") List<Long> idList) {
        return systemUserService.getSystemUserList(idList);
    }

    @PostMapping("/query")
    @Operation(summary = "分页查询用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public DataGrid<SystemUserVO> getSystemUserPage(@Validated @RequestBody SystemUserQueryRequest request) {
        return systemUserService.getSystemUserPage(request);
    }

    @PutMapping("/delete")
    @Operation(summary = "通过 id 删除用户")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:delete')")
    public Integer deleteSystemUser(@RequestParam("id") Long id) {
        return systemUserService.deleteSystemUser(id);
    }

}

