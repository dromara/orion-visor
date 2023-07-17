package com.orion.ops.module.infra.service.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.module.infra.convert.SystemMenuConvert;
import com.orion.ops.module.infra.dao.SystemMenuDAO;
import com.orion.ops.module.infra.dao.SystemRoleDAO;
import com.orion.ops.module.infra.dao.SystemRoleMenuDAO;
import com.orion.ops.module.infra.define.RoleDefine;
import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.domain.SystemRoleDO;
import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuCacheDTO;
import com.orion.ops.module.infra.enums.MenuStatusEnum;
import com.orion.ops.module.infra.enums.RoleStatusEnum;
import com.orion.ops.module.infra.service.PermissionService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 权限服务
 * <p>
 * TODO 分布式缓存解决方案?
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:05
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    /**
     * 菜单缓存
     */
    @Getter
    private final Map<String, SystemRoleDO> roleCache = new HashMap<>();

    /**
     * 菜单缓存 以作角色权限直接引用
     *
     * @see #roleMenuCache
     */
    @Getter
    private final List<SystemMenuCacheDTO> menuCache = new ArrayList<>();

    /**
     * 角色菜单关联
     */
    @Getter
    private final Map<String, List<SystemMenuCacheDTO>> roleMenuCache = new HashMap<>();

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
        log.info("initRoleMenuCache-start");
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
        log.info("initRoleMenuCache-end used: {}ms", System.currentTimeMillis() - start);
    }

    @Override
    public boolean rolesHasRole(List<String> roles, String role) {
        // 检查是否为超级管理员
        for (String r : roles) {
            // 是否为超级管理员
            if (RoleDefine.isAdmin(r) && this.checkRoleEnabled(r)) {
                return true;
            }
        }
        // 检查是否包含
        if (!roles.contains(role)) {
            return false;
        }
        // 检查是否启用
        return this.checkRoleEnabled(role);
    }

    @Override
    public boolean rolesHasPermission(List<String> roles, String permission) {
        // 检查是否为超级管理员
        for (String r : roles) {
            // 是否为超级管理员
            if (RoleDefine.isAdmin(r) && this.checkRoleEnabled(r)) {
                return true;
            }
        }
        // 检查普通角色是否有此权限
        for (String role : roles) {
            // 角色是否启用
            if (!this.checkRoleEnabled(role)) {
                continue;
            }
            // 获取角色权限列表
            List<SystemMenuCacheDTO> menus = roleMenuCache.get(role);
            if (Lists.isEmpty(menus)) {
                continue;
            }
            boolean has = menus.stream()
                    .filter(s -> MenuStatusEnum.ENABLED.getStatus().equals(s.getStatus()))
                    .map(SystemMenuCacheDTO::getPermission)
                    .filter(Objects::nonNull)
                    .anyMatch(permission::equals);
            if (has) {
                return true;
            }
        }
        return false;
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
