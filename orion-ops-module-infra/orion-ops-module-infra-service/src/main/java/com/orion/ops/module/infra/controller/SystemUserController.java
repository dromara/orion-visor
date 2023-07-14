package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.SystemUserCreateRequest;
import com.orion.ops.module.infra.entity.request.SystemUserQueryRequest;
import com.orion.ops.module.infra.entity.request.SystemUserUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
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

    @PutMapping("/delete-batch")
    @Operation(summary = "通过 id 批量删除用户")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:delete')")
    public Integer batchDeleteSystemUser(@RequestParam("idList") List<Long> idList) {
        return systemUserService.batchDeleteSystemUser(idList);
    }

}

