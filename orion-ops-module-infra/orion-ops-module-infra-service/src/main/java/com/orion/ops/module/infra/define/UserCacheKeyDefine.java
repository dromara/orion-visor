package com.orion.ops.module.infra.define;

import com.orion.lang.define.cache.CacheKeyDefine;

import java.util.concurrent.TimeUnit;

/**
 * 用户模块缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 21:54
 */
public interface UserCacheKeyDefine {

    CacheKeyDefine USER_INFO = new CacheKeyDefine("user:info:{}", "用户信息 ${id}");

    CacheKeyDefine LOGIN_FAILED_COUNT = new CacheKeyDefine("user:failed:{}", "用户登陆失败次数 ${username}", 3, TimeUnit.DAYS);

    CacheKeyDefine LOGIN_TOKEN = new CacheKeyDefine("user:token:{}:{}", "用户登陆 token ${id} ${time}", 24, TimeUnit.HOURS);

    CacheKeyDefine LOGIN_REFRESH = new CacheKeyDefine("user:refresh:{}:{}", "用户刷新 token ${id} ${time}", 28, TimeUnit.HOURS);

}
