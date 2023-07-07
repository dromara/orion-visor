package com.orion.ops.launch.service;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.security.core.service.SecurityFrameworkService;
import org.springframework.stereotype.Component;

/**
 * TODO 基建模块实现 现在默认实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 10:57
 */
@Component
public class EmptySecurityImpl implements SecurityFrameworkService {

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public LoginUser getUserByToken(String token) {
        // TODO MOCK
        LoginUser user = new LoginUser();
        user.setId(123L);
        user.setUsername("username");
        user.setNickname("nickname");
        user.setRoles(Lists.of("r1", "r2"));
        return user;
    }

}
