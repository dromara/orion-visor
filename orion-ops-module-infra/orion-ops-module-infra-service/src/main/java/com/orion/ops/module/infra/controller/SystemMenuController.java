package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemMenuVO;
import com.orion.ops.module.infra.service.SystemMenuService;
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
 * 菜单 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Tag(name = "infra - 菜单服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-menu")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class SystemMenuController {

    @Resource
    private SystemMenuService systemMenuService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:create')")
    public Long createSystemMenu(@Validated @RequestBody SystemMenuCreateRequest request) {
        return systemMenuService.createSystemMenu(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:update')")
    public Integer updateSystemMenu(@Validated @RequestBody SystemMenuUpdateRequest request) {
        return systemMenuService.updateSystemMenu(request);
    }

    @GetMapping("/get")
    @Operation(summary = "通过 id 查询菜单")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-menu:query')")
    public SystemMenuVO getSystemMenu(@RequestParam("id") Long id) {
        return systemMenuService.getSystemMenu(id);
    }

    @GetMapping("/list")
    @Operation(summary = "通过 id 批量查询菜单")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-menu:query')")
    public List<SystemMenuVO> getSystemMenuList(@RequestParam("idList") List<Long> idList) {
        return systemMenuService.getSystemMenuList(idList);
    }

    @PostMapping("/query")
    @Operation(summary = "分页查询菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:query')")
    public DataGrid<SystemMenuVO> getSystemMenuPage(@Validated @RequestBody SystemMenuQueryRequest request) {
        return systemMenuService.getSystemMenuPage(request);
    }

    @PutMapping("/delete")
    @Operation(summary = "通过 id 删除菜单")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-menu:delete')")
    public Integer deleteSystemMenu(@RequestParam("id") Long id) {
        return systemMenuService.deleteSystemMenu(id);
    }

    @PutMapping("/delete-batch")
    @Operation(summary = "通过 id 批量删除菜单")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-menu:delete')")
    public Integer batchDeleteSystemMenu(@RequestParam("idList") List<Long> idList) {
        return systemMenuService.batchDeleteSystemMenu(idList);
    }

}

