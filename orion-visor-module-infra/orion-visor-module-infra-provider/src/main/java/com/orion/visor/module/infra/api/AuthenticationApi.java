package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.user.SystemUserAuthDTO;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/14 21:37
 */
public interface AuthenticationApi {

    /**
     * 通过密码认证
     *
     * @param username       username
     * @param password       password
     * @param addFailedCount addFailedCount
     * @return result
     */
    SystemUserAuthDTO authByPassword(String username, String password, boolean addFailedCount);

}
