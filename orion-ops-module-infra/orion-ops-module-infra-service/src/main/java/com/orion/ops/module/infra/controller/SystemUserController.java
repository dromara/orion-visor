package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.define.operator.SystemUserOperatorType;
import com.orion.ops.module.infra.entity.request.user.*;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import com.orion.ops.module.infra.entity.vo.UserSessionVO;
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

    @OperatorLog(SystemUserOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:create')")
    public Long createSystemUser(@Validated @RequestBody SystemUserCreateRequest request) {
        return systemUserService.createSystemUser(request);
    }

    @OperatorLog(SystemUserOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update')")
    public Integer updateSystemUser(@Validated @RequestBody SystemUserUpdateRequest request) {
        return systemUserService.updateSystemUserById(request);
    }

    // TODO 修改头像 最后再说 可有可无的功能 要是有 http 文件需求就写

    @OperatorLog(SystemUserOperatorType.UPDATE_STATUS)
    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update-status')")
    public Integer updateUserStatus(@Validated @RequestBody SystemUserUpdateStatusRequest request) {
        return systemUserService.updateUserStatus(request);
    }

    @OperatorLog(SystemUserOperatorType.GRANT_ROLE)
    @PutMapping("/grant-role")
    @Operation(summary = "分配用户角色")
    @PreAuthorize("@ss.hasPermission('infra:system-user:grant-role')")
    public Integer updateUserRole(@Validated @RequestBody SystemUserUpdateRoleRequest request) {
        if (Lists.isEmpty(request.getRoleIdList())) {
            // 删除用户角色
            return systemUserRoleService.deleteUserRoles(request);
        } else {
            // 更新用户角色
            return systemUserRoleService.updateUserRoles(request);
        }
    }

    @OperatorLog(SystemUserOperatorType.RESET_PASSWORD)
    @PutMapping("/reset-password")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@ss.hasPermission('infra:system-user:reset-password')")
    public HttpWrapper<?> resetUserPassword(@Validated @RequestBody UserResetPasswordRequest request) {
        systemUserService.resetPassword(request);
        return HttpWrapper.ok();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询用户")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public SystemUserVO getSystemUser(@RequestParam("id") Long id) {
        return systemUserService.getSystemUserById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询所有用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public List<SystemUserVO> getSystemUserList() {
        return systemUserService.getSystemUserByIdList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-roles")
    @Operation(summary = "查询用户的角色id")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public List<Long> getUserRoleIdList(@RequestParam("userId") Long userId) {
        return systemUserRoleService.getUserRoleIdList(userId);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public DataGrid<SystemUserVO> getSystemUserPage(@Validated(Page.class) @RequestBody SystemUserQueryRequest request) {
        return systemUserService.getSystemUserPage(request);
    }

    @OperatorLog(SystemUserOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除用户")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:delete')")
    public Integer deleteSystemUser(@RequestParam("id") Long id) {
        return systemUserService.deleteSystemUserById(id);
    }

    // fixme 权限配置
    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/user-session")
    @Operation(summary = "获取用户会话列表")
    public List<UserSessionVO> getUserSessionList(@RequestParam("id") Long id) {
        return systemUserService.getUserSessionList(id);
    }

    // fixme 权限配置
    @IgnoreLog(IgnoreLogMode.RET)
    @PutMapping("/offline-session")
    @Operation(summary = "下线用户会话")
    public HttpWrapper<?> offlineUserSession(@Validated @RequestBody OfflineUserSessionRequest request) {
        systemUserService.offlineUserSession(request);
        return HttpWrapper.ok();
    }

}

