package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.menu.SystemRoleBindMenuRequest;

import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
public interface SystemRoleMenuService {

    /**
     * 绑定角色菜单
     *
     * @param request request
     * @return effect
     */
    Integer bindRoleMenu(SystemRoleBindMenuRequest request);

    /**
     * 获取角色菜单 id
     *
     * @param roleId roleId
     * @return menuIdList
     */
    List<Long> getRoleMenuIdList(Long roleId);

}
