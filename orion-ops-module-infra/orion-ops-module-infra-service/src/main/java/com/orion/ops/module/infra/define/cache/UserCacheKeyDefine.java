package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;

import java.util.concurrent.TimeUnit;

/**
 * 用户模块缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 21:54
 */
public interface UserCacheKeyDefine {

    CacheKeyDefine USER_INFO = new CacheKeyBuilder()
            .key("user:info:{}")
            .desc("用户信息 ${id}")
            .type(LoginUser.class)
            .build();

    CacheKeyDefine LOGIN_FAILED_COUNT = new CacheKeyBuilder()
            .key("user:failed:{}")
            .desc("用户登陆失败次数 ${username}")
            .timeout(3, TimeUnit.DAYS)
            .build();

    CacheKeyDefine LOGIN_TOKEN = new CacheKeyBuilder()
            .key("user:token:{}:{}")
            .desc("用户登陆 token ${id} ${time}")
            .type(LoginTokenDTO.class)
            .timeout(24, TimeUnit.HOURS)
            .build();

    CacheKeyDefine LOGIN_REFRESH = new CacheKeyBuilder()
            .key("user:refresh:{}:{}")
            .desc("用户刷新 token ${id} ${time}")
            .type(LoginTokenDTO.class)
            .timeout(28, TimeUnit.HOURS)
            .build();

}
