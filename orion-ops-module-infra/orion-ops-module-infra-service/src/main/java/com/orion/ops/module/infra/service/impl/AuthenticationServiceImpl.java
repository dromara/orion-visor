package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.request.UserLoginRequest;
import com.orion.ops.module.infra.service.AuthenticationService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 22:15
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * 允许多端登陆
     */
    private final boolean allowMultiPlatform = false;

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UserLoginRequest request) {
        // 检查登陆
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                .eq(SystemUserDO::getUsername, request.getUsername())
                .eq(SystemUserDO::getPassword, Signatures.md5(request.getPassword()));
        // 获取登陆用户
        Optional<SystemUserDO> systemUserDO = systemUserDAO.of(wrapper).only().get();
        Valid.isTrue(systemUserDO.isPresent(), ErrorMessage.USERNAME_PASSWORD_ERROR);
        // 检查用户状态

        // 设置缓存

        // 不允许多端登陆删除缓存

        // 生成 authenticationToken

        // 生成 refreshToken

        // 

        return null;
    }

    @Override
    public void logout(String token) {

    }

}
