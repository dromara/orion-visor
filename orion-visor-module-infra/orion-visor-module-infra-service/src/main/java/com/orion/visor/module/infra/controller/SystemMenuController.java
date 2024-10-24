/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.infra.controller;

import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.DemoDisableApi;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.define.operator.SystemMenuOperatorType;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuUpdateRequest;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuUpdateStatusRequest;
import com.orion.visor.module.infra.entity.vo.SystemMenuVO;
import com.orion.visor.module.infra.service.SystemMenuService;
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
public class SystemMenuController {

    @Resource
    private SystemMenuService systemMenuService;

    @DemoDisableApi
    @OperatorLog(SystemMenuOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:create')")
    public Long createSystemMenu(@RequestBody SystemMenuCreateRequest request) {
        return systemMenuService.createSystemMenu(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemMenuOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:update')")
    public Integer updateSystemMenu(@RequestBody SystemMenuUpdateRequest request) {
        return systemMenuService.updateSystemMenuById(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemMenuOperatorType.UPDATE_STATUS)
    @PutMapping("/update-status")
    @Operation(summary = "通过 id 级联更新菜单状态")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:update-status')")
    public Integer updateSystemMenuStatus(@RequestBody SystemMenuUpdateStatusRequest request) {
        return systemMenuService.updateSystemMenuStatus(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询菜单")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-menu:query')")
    public SystemMenuVO getSystemMenu(@RequestParam("id") Long id) {
        return systemMenuService.getSystemMenuById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "查询菜单")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:query')")
    public List<SystemMenuVO> getSystemMenuList(@Validated @RequestBody SystemMenuQueryRequest request) {
        return systemMenuService.getSystemMenuByIdList(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemMenuOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 级联删除菜单")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-menu:delete')")
    public Integer deleteSystemMenu(@RequestParam("id") Long id) {
        return systemMenuService.deleteSystemMenu(id);
    }

}

