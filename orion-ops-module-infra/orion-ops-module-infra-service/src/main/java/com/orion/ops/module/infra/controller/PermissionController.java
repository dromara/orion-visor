package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.common.annotation.IgnoreLog;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.framework.common.constant.IgnoreLogMode;
import com.orion.ops.module.infra.entity.vo.SystemMenuVO;
import com.orion.ops.module.infra.entity.vo.UserPermissionVO;
import com.orion.ops.module.infra.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:20
 */
@Tag(name = "infra - 权限服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/permission")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping("/init-cache")
    @Operation(summary = "初始化角色权限缓存")
    @PreAuthorize("@ss.hasPermission('infra:system:init-permission-cache')")
    public HttpWrapper<?> initCache() {
        permissionService.initPermissionCache();
        return HttpWrapper.ok();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/menu")
    @Operation(summary = "获取用户菜单")
    public List<SystemMenuVO> getUserMenuList() {
        return permissionService.getUserMenuList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/permission")
    @Operation(summary = "获取用户权限")
    public UserPermissionVO getUserPermission() {
        return permissionService.getUserPermission();
    }

}
