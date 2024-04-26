package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.entity.dto.UserInfoDTO;

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
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine USER_LIST = new CacheKeyBuilder()
            .key("user:base:list")
            .desc("用户列表")
            .type(UserInfoDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine LOGIN_FAILED_COUNT = new CacheKeyBuilder()
            .key("user:login-failed:{}")
            .desc("用户登录失败次数 ${username}")
            .type(Integer.class)
            .struct(RedisCacheStruct.STRING)
            .build();

    CacheKeyDefine LOGIN_TOKEN = new CacheKeyBuilder()
            .key("user:token:{}:{}")
            .desc("用户登录 token ${id} ${time}")
            .type(LoginTokenDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(24, TimeUnit.HOURS)
            .build();

    CacheKeyDefine LOGIN_REFRESH = new CacheKeyBuilder()
            .key("user:refresh:{}:{}")
            .desc("用户刷新 token ${id} ${time}")
            .type(LoginTokenDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(32, TimeUnit.HOURS)
            .build();

}
