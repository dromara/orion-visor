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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.security.LoginUser;
import org.dromara.visor.framework.common.security.UserRole;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.convert.SystemMenuConvert;
import org.dromara.visor.module.infra.convert.SystemUserConvert;
import org.dromara.visor.module.infra.dao.SystemMenuDAO;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.dao.SystemRoleMenuDAO;
import org.dromara.visor.module.infra.define.RoleDefine;
import org.dromara.visor.module.infra.entity.domain.SystemMenuDO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleMenuDO;
import org.dromara.visor.module.infra.entity.dto.SystemMenuCacheDTO;
import org.dromara.visor.module.infra.entity.vo.SystemMenuVO;
import org.dromara.visor.module.infra.entity.vo.UserCollectInfoVO;
import org.dromara.visor.module.infra.entity.vo.UserPermissionVO;
import org.dromara.visor.module.infra.enums.MenuStatusEnum;
import org.dromara.visor.module.infra.enums.MenuTypeEnum;
import org.dromara.visor.module.infra.enums.PreferenceTypeEnum;
import org.dromara.visor.module.infra.enums.RoleStatusEnum;
import org.dromara.visor.module.infra.service.PreferenceService;
import org.dromara.visor.module.infra.service.SystemMenuService;
import org.dromara.visor.module.infra.service.TipsService;
import org.dromara.visor.module.infra.service.UserPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
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

    @Resource
    private SystemMenuService systemMenuService;

    @Resource
    private PreferenceService preferenceService;

    @Resource
    private TipsService tipsService;

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
        // 检查是否为超级管理员或包含此角色
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
        // 获取用户角色
        Map<Long, String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员
        if (RoleDefine.containsAdmin(roles.values())) {
            return true;
        }
        // 检查普通角色是否有此权限
        return roles.keySet()
                .stream()
                .anyMatch(s -> this.checkRoleHasPermission(s, permission));
    }

    @Override
    public boolean hasAnyPermission(String... permissions) {
        if (Arrays1.isEmpty(permissions)) {
            return true;
        }
        // 获取用户角色
        Map<Long, String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员
        if (RoleDefine.containsAdmin(roles.values())) {
            return true;
        }
        // 检查用户角色是否包含权限
        return Arrays.stream(permissions)
                .anyMatch(perm -> roles.keySet()
                        .stream()
                        .anyMatch(s -> this.checkRoleHasPermission(s, perm)));
    }

    @Override
    public List<SystemMenuVO> getUserMenuList() {
        // 获取用户角色
        Map<Long, String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return Lists.empty();
        }
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
        // 状态过滤
        List<SystemMenuVO> menus = mergeStream
                .filter(s -> MenuStatusEnum.ENABLED.getStatus().equals(s.getStatus()))
                .filter(s -> !MenuTypeEnum.FUNCTION.getType().equals(s.getType()))
                .map(SystemMenuConvert.MAPPER::to)
                .collect(Collectors.toList());
        // 构建菜单树
        return systemMenuService.buildSystemMenuTree(menus);
    }

    @SneakyThrows
    @Override
    public UserPermissionVO getUserPermission() {
        // 获取用户信息
        UserCollectInfoVO user = SystemUserConvert.MAPPER.toCollectInfo(SecurityUtils.getLoginUser());
        Long id = user.getId();
        // 获取用户系统偏好
        Future<Map<String, Object>> systemPreference = preferenceService.getPreferenceAsync(id, PreferenceTypeEnum.SYSTEM);
        // 获取用户角色
        Map<Long, String> roles = this.getUserEnabledRoles();
        // 获取用户权限
        List<String> permissions;
        if (roles.isEmpty()) {
            permissions = Lists.empty();
        } else {
            if (RoleDefine.containsAdmin(roles.values())) {
                // 管理员拥有全部权限
                permissions = Lists.of(Const.ASTERISK);
            } else {
                // 当前用户所适配的角色的权限
                permissions = roles.keySet()
                        .stream()
                        .map(roleMenuCache::get)
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .filter(s -> MenuStatusEnum.ENABLED.getStatus().equals(s.getStatus()))
                        .map(SystemMenuCacheDTO::getPermission)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }
        // 设置已提示的 key
        user.setTippedKeys(tipsService.getTippedKeys());
        // 获取异步结果
        user.setSystemPreference(systemPreference.get());
        // 组装数据
        return UserPermissionVO.builder()
                .user(user)
                .roles(roles.values())
                .permissions(permissions)
                .build();
    }

    /**
     * 检查角色是否有权限
     *
     * @param roleId     roleId
     * @param permission permission
     * @return 是否有权限
     */
    private boolean checkRoleHasPermission(Long roleId, String permission) {
        // 获取角色权限列表
        List<SystemMenuCacheDTO> menus = roleMenuCache.get(roleId);
        if (Lists.isEmpty(menus)) {
            return false;
        }
        // 检查是否有此权限
        return menus.stream()
                .filter(s -> MenuStatusEnum.ENABLED.getStatus().equals(s.getStatus()))
                .map(SystemMenuCacheDTO::getPermission)
                .filter(Objects::nonNull)
                .anyMatch(permission::equals);
    }

    /**
     * 获取用户启用的角色
     *
     * @return roles
     */
    private Map<Long, String> getUserEnabledRoles() {
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

}
