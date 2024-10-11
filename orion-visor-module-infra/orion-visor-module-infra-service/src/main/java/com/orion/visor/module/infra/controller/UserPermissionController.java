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

import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.entity.vo.SystemMenuVO;
import com.orion.visor.module.infra.entity.vo.UserPermissionVO;
import com.orion.visor.module.infra.service.UserPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@Tag(name = "infra - 用户权限服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/user-permission")
public class UserPermissionController {

    @Resource
    private UserPermissionService userPermissionService;

    @PutMapping("/refresh-cache")
    @Operation(summary = "刷新角色权限缓存")
    @PreAuthorize("@ss.hasPermission('infra:system-menu:management:refresh-cache')")
    public Boolean refreshCache() {
        userPermissionService.initPermissionCache();
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/menu")
    @Operation(summary = "获取用户菜单")
    public List<SystemMenuVO> getUserMenuList() {
        return userPermissionService.getUserMenuList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/user")
    @Operation(summary = "获取用户权限聚合信息")
    public UserPermissionVO getUserPermission() {
        return userPermissionService.getUserPermission();
    }

}
