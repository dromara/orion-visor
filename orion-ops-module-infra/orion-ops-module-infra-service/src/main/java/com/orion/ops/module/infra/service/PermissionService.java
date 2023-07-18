package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.domain.SystemRoleDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuCacheDTO;

import java.util.List;
import java.util.Map;

/**
 * 权限服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:03
 */
public interface PermissionService {

    /**
     * 获取 角色缓存
     *
     * @return cache
     */
    Map<String, SystemRoleDO> getRoleCache();

    /**
     * 获取 菜单缓存 以作角色权限直接引用
     *
     * @return cache
     */
    List<SystemMenuCacheDTO> getMenuCache();

    /**
     * 获取 角色菜单关联
     *
     * @return cache
     */
    Map<String, List<SystemMenuCacheDTO>> getRoleMenuCache();

    /**
     * 初始化权限缓存
     */
    void initPermissionCache();

    /**
     * 检查角色是否含有此角色 (有效性判断)
     *
     * @param roles roles
     * @param role  role
     * @return 是否包含
     */
    boolean rolesHasRole(List<String> roles, String role);

    /**
     * 检查角色是否含有此权限 (有效性判断)
     *
     * @param roles      roles
     * @param permission permission
     * @return 是否包含
     */
    boolean rolesHasPermission(List<String> roles, String permission);

}
