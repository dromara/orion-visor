package com.orion.ops.module.infra.service.impl;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.convert.SystemMenuConvert;
import com.orion.ops.module.infra.convert.SystemUserConvert;
import com.orion.ops.module.infra.dao.SystemMenuDAO;
import com.orion.ops.module.infra.dao.SystemRoleDAO;
import com.orion.ops.module.infra.dao.SystemRoleMenuDAO;
import com.orion.ops.module.infra.define.RoleDefine;
import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.domain.SystemRoleDO;
import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuCacheDTO;
import com.orion.ops.module.infra.entity.vo.SystemMenuVO;
import com.orion.ops.module.infra.entity.vo.UserCollectInfoVO;
import com.orion.ops.module.infra.entity.vo.UserPermissionVO;
import com.orion.ops.module.infra.enums.MenuStatusEnum;
import com.orion.ops.module.infra.enums.MenuTypeEnum;
import com.orion.ops.module.infra.enums.PreferenceTypeEnum;
import com.orion.ops.module.infra.enums.RoleStatusEnum;
import com.orion.ops.module.infra.service.PermissionService;
import com.orion.ops.module.infra.service.PreferenceService;
import com.orion.ops.module.infra.service.SystemMenuService;
import com.orion.ops.module.infra.service.TipsService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 权限服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:05
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Getter
    private final Map<String, SystemRoleDO> roleCache = new HashMap<>();

    @Getter
    private final List<SystemMenuCacheDTO> menuCache = new ArrayList<>();

    @Getter
    private final Map<String, List<SystemMenuCacheDTO>> roleMenuCache = new HashMap<>();

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
        Map<Long, SystemRoleDO> roleRel = roles.stream()
                .collect(Collectors.toMap(SystemRoleDO::getId, Function.identity()));
        for (SystemRoleDO role : roles) {
            roleCache.put(role.getCode(), role);
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
                .forEach((rid, mids) -> {
                    // 获取菜单引用
                    List<SystemMenuCacheDTO> roleMenus = mids.stream()
                            .map(menuMapping::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    // 获取角色引用
                    Optional.ofNullable(rid)
                            .map(roleRel::get)
                            .map(SystemRoleDO::getCode)
                            .ifPresent(code -> roleMenuCache.put(code, roleMenus));
                });
        log.info("initPermissionCache-end used: {}ms", System.currentTimeMillis() - start);
    }

    @Override
    public boolean hasRole(String role) {
        // 获取用户角色
        List<String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员或包含此角色
        return RoleDefine.containsAdmin(roles) || roles.contains(role);
    }

    @Override
    public boolean hasPermission(String permission) {
        // 获取用户角色
        List<String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员
        if (RoleDefine.containsAdmin(roles)) {
            return true;
        }
        // 检查普通角色是否有此权限
        return roles.stream().anyMatch(s -> this.checkRoleHasPermission(s, permission));
    }

    @Override
    public boolean hasAnyPermission(String... permissions) {
        if (Arrays1.isEmpty(permissions)) {
            return true;
        }
        // 获取用户角色
        List<String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return false;
        }
        // 检查是否为超级管理员
        if (RoleDefine.containsAdmin(roles)) {
            return true;
        }
        for (String permission : permissions) {
            final boolean has = roles.stream().anyMatch(s -> this.checkRoleHasPermission(s, permission));
            if (has) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<SystemMenuVO> getUserMenuList() {
        // 获取用户角色
        List<String> roles = this.getUserEnabledRoles();
        if (roles.isEmpty()) {
            return Lists.empty();
        }
        // 查询角色菜单
        Stream<SystemMenuCacheDTO> mergeStream;
        if (RoleDefine.containsAdmin(roles)) {
            // 管理员拥有全部菜单
            mergeStream = menuCache.stream();
        } else {
            // 当前用户所适配的角色菜单
            mergeStream = roles.stream()
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
        List<String> roles = this.getUserEnabledRoles();
        // 获取用户权限
        List<String> permissions;
        if (roles.isEmpty()) {
            permissions = Lists.empty();
        } else {
            if (RoleDefine.containsAdmin(roles)) {
                // 管理员拥有全部权限
                permissions = Lists.of(Const.ASTERISK);
            } else {
                // 当前用户所适配的角色的权限
                permissions = roles.stream()
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
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    /**
     * 检查角色是否有权限
     *
     * @param role       role
     * @param permission permission
     * @return 是否有权限
     */
    private boolean checkRoleHasPermission(String role, String permission) {
        // 获取角色权限列表
        List<SystemMenuCacheDTO> menus = roleMenuCache.get(role);
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
    private List<String> getUserEnabledRoles() {
        // 获取当前用户角色
        List<String> roles = Optional.ofNullable(SecurityUtils.getLoginUser())
                .map(LoginUser::getRoles)
                .orElse(Lists.empty());
        if (Lists.isEmpty(roles)) {
            return Lists.empty();
        }
        // 过滤未启用的角色
        return roles.stream()
                .filter(this::checkRoleEnabled)
                .collect(Collectors.toList());
    }

    /**
     * 检查角色是否启用
     *
     * @param role role
     * @return 是否启用
     */
    private boolean checkRoleEnabled(String role) {
        SystemRoleDO systemRole = roleCache.get(role);
        if (systemRole == null) {
            return false;
        }
        return RoleStatusEnum.ENABLED.getStatus().equals(systemRole.getStatus());
    }

}
