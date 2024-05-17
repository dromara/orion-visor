package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.user.SystemUserDTO;

/**
 * 用户服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 15:15
 */
public interface SystemUserApi {

    /**
     * 通过 id 查询用户
     *
     * @param id id
     * @return user
     */
    SystemUserDTO getUserById(Long id);

    /**
     * 用户是否为管理员用户
     *
     * @param id id
     * @return isAdmin
     */
    boolean isAdminUser(Long id);

}
