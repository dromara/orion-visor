package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.menu.SystemMenuBindRequest;

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
    Integer bindRoleMenu(SystemMenuBindRequest request);

}
