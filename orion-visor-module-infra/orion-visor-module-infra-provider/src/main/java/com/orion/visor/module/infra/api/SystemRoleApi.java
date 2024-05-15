package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.role.SystemRoleDTO;

/**
 * 角色服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 15:16
 */
public interface SystemRoleApi {

    /**
     * 查询角色
     *
     * @param id id
     * @return role
     */
    SystemRoleDTO getRoleById(Long id);

}
