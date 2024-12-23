/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.common.validator.group.Page;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.SystemRoleOperatorType;
import org.dromara.visor.module.infra.entity.request.menu.SystemRoleGrantMenuRequest;
import org.dromara.visor.module.infra.entity.request.role.SystemRoleCreateRequest;
import org.dromara.visor.module.infra.entity.request.role.SystemRoleQueryRequest;
import org.dromara.visor.module.infra.entity.request.role.SystemRoleStatusRequest;
import org.dromara.visor.module.infra.entity.request.role.SystemRoleUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.SystemRoleVO;
import org.dromara.visor.module.infra.service.SystemRoleMenuService;
import org.dromara.visor.module.infra.service.SystemRoleService;
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
public class SystemRoleController {

    @Resource
    private SystemRoleService systemRoleService;

    @Resource
    private SystemRoleMenuService systemRoleMenuService;

    @DemoDisableApi
    @OperatorLog(SystemRoleOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:create')")
    public Long createSystemRole(@Validated @RequestBody SystemRoleCreateRequest request) {
        return systemRoleService.createSystemRole(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemRoleOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新角色")
    @PreAuthorize("@ss.hasPermission('infra:system-role:update')")
    public Integer updateSystemRole(@Validated @RequestBody SystemRoleUpdateRequest request) {
        return systemRoleService.updateSystemRoleById(request);
    }

    @DemoDisableApi
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

    @DemoDisableApi
    @OperatorLog(SystemRoleOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除角色")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-role:delete')")
    public Integer deleteSystemRole(@RequestParam("id") Long id) {
        return systemRoleService.deleteSystemRoleById(id);
    }

    @DemoDisableApi
    @OperatorLog(SystemRoleOperatorType.GRANT_MENU)
    @PutMapping("/grant-menu")
    @Operation(summary = "分配角色菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-role:grant-menu')")
    public Integer grantRoleMenu(@RequestBody SystemRoleGrantMenuRequest request) {
        return systemRoleMenuService.grantRoleMenu(request);
    }

}

