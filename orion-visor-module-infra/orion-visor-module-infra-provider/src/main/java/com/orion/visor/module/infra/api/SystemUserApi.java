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
     * 通过 id 查询用户名
     *
     * @param id id
     * @return username
     */
    String getUsernameById(Long id);

    /**
     * 通过 id 查询花名
     *
     * @param id id
     * @return nickname
     */
    String getNicknameById(Long id);

    /**
     * 通过 id 查询用户
     *
     * @param id id
     * @return user
     */
    SystemUserDTO getUserById(Long id);

}
