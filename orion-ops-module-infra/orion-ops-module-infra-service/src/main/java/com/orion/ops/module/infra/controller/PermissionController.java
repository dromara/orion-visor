package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.common.annotation.RestWrapper;
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
    @Operation(summary = "初始化缓存")
    @PreAuthorize("@ss.hasPermission('infra:system-role:init')")
    public HttpWrapper<?> initCache() {
        permissionService.initPermissionCache();
        return HttpWrapper.ok();
    }

}
