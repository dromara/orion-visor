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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.common.security.UserRole;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.convert.SystemMenuConvert;
import org.dromara.visor.module.infra.dao.SystemMenuDAO;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.dao.SystemRoleMenuDAO;
import org.dromara.visor.module.infra.define.RoleDefine;
import org.dromara.visor.module.infra.entity.domain.SystemMenuDO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleMenuDO;
import org.dromara.visor.module.infra.entity.dto.SystemMenuCacheDTO;
import org.dromara.visor.module.infra.enums.MenuStatusEnum;
import org.dromara.visor.module.infra.enums.RoleStatusEnum;
import org.dromara.visor.module.infra.service.UserPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户权限服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:05
 */
@Slf4j
@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Getter
    private final Map<Long, SystemRoleDO> roleCache = new HashMap<>();

    @Getter
    private final List<SystemMenuCacheDTO> menuCache = new ArrayList<>();

    @Getter
    private final Map<Long, List<SystemMenuCacheDTO>> roleMenuCache = new HashMap<>();

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Resource
    private SystemMenuDAO systemMenuDAO;

    @Resource
    private SystemRoleMenuDAO systemRoleMenuDAO;

    @PostConstruct
    @Override
    public void initPermissionCache() {
        long start = System.currentTimeMillis();
        log.info("initPermissionCache-start");
        roleCache.clear();
        menuCache.clear();
        roleMenuCache.clear();
        // 加载所有角色
        List<SystemRoleDO> roles = systemRoleDAO.selectList(null);
        for (SystemRoleDO role : roles) {
            roleCache.put(role.getId(), role);
        }
        // 加载所有菜单信息
        List<SystemMenuDO> menuList = systemMenuDAO.selectList(null);
        List<SystemMenuCacheDTO> menus = SystemMenuConvert.MAPPER.toCache(menuList);
        Map<Long, SystemMenuCacheDTO> menuMapping = menus.stream()
                .collect(Collectors.toMap(SystemMenuCacheDTO::getId, Function.identity()));
        menuCache.addAll(menus);
        // 查询所有角色菜单
        systemRoleMenuDAO.selectList(null)
                .stream()
                .collect(Collectors.groupingBy(SystemRoleMenuDO::getRoleId,
                        Collectors.mapping(SystemRoleMenuDO::getMenuId, Collectors.toList())))
                .forEach((roleId, menuIdList) -> {
                    // 获取菜单引用
                    List<SystemMenuCacheDTO> roleMenus = menuIdList.stream()
                            .map(menuMapping::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    // 获取角色引用
                    roleMenuCache.put(roleId, roleMenus);
                });
        log.info("initPermissionCache-end used: {}ms", System.currentTimeMillis() - start);
    }

    @Override
    public boolean hasRole(String role) {
        // 获取用户角色
        Map<Long, String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员 || 包含此角色
        return RoleDefine.containsAdmin(roles.values()) || roles.containsValue(role);
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        if (Arrays1.isEmpty(roles)) {
            return true;
        }
        // 获取用户角色
        Map<Long, String> enableRoles = this.getUserEnabledRoles();
        if (enableRoles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员 || 有此角色
        return RoleDefine.containsAdmin(enableRoles.values())
                || Arrays.stream(roles).anyMatch(enableRoles::containsValue);
    }

    @Override
    public boolean hasPermission(String permission) {
        // 获取用户权限
        List<String> userPermissions = this.getMenuPermissions(this.getUserEnabledMenus());
        // 检查权限
        return userPermissions.contains(permission);
    }

    @Override
    public boolean hasAnyPermission(String... permissions) {
        if (Arrays1.isEmpty(permissions)) {
            return true;
        }
        // 获取用户权限
        List<String> userPermissions = this.getMenuPermissions(this.getUserEnabledMenus());
        // 检查权限
        return Arrays.stream(permissions).anyMatch(userPermissions::contains);
    }

    @Override
    public Map<Long, String> getUserEnabledRoles() {
        // 获取当前用户角色
        List<UserRole> userRoles = Optional.ofNullable(SecurityUtils.getLoginUser())
                .map(LoginUser::getRoles)
                .orElse(Lists.empty());
        if (Lists.isEmpty(userRoles)) {
            return Maps.empty();
        }
        // 获取角色编码
        Map<Long, String> roles = userRoles.stream()
                .map(UserRole::getId)
                .map(roleCache::get)
                .filter(Objects::nonNull)
                // 过滤未启用的角色
                .filter(r -> RoleStatusEnum.ENABLED.getStatus().equals(r.getStatus()))
                .collect(Collectors.toMap(SystemRoleDO::getId, SystemRoleDO::getCode));
        if (Maps.isEmpty(roles)) {
            return Maps.empty();
        }
        return roles;
    }

    @Override
    public List<SystemMenuCacheDTO> getUserEnabledMenus() {
        // 获取用户角色
        Map<Long, String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return Lists.empty();
        }
        // 获取角色菜单
        return this.getRolesEnabledMenus(roles);
    }

    @Override
    public List<SystemMenuCacheDTO> getRolesEnabledMenus(Map<Long, String> roles) {
        // 查询角色菜单
        Stream<SystemMenuCacheDTO> mergeStream;
        if (RoleDefine.containsAdmin(roles.values())) {
            // 管理员拥有全部菜单
            mergeStream = menuCache.stream();
        } else {
            // 当前用户所适配的角色菜单
            mergeStream = roles.keySet()
                    .stream()
                    .map(roleMenuCache::get)
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .distinct();
        }
        // 过滤未启用的权限
        return mergeStream
                .filter(s -> MenuStatusEnum.ENABLED.getStatus().equals(s.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMenuPermissions(List<SystemMenuCacheDTO> menus) {
        if (menus.isEmpty()) {
            return Lists.empty();
        }
        return menus.stream()
                .map(SystemMenuCacheDTO::getPermission)
                .filter(Strings::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

}
