package com.orion.ops.module.infra.define;

import com.orion.lang.define.cache.CacheKeyDefine;

import java.util.concurrent.TimeUnit;

/**
 * 基建模块缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 21:54
 */
public interface InfraCacheKeyDefine {

    CacheKeyDefine USER_INFO = new CacheKeyDefine("user:info:{}", "用户信息", 30, TimeUnit.DAYS);

    CacheKeyDefine USER_TOKEN = new CacheKeyDefine("user:token:{}", "用户认证 authenticationToken", 48, TimeUnit.HOURS);

    CacheKeyDefine USER_REFRESH = new CacheKeyDefine("user:refresh:{}", "用户认证 refreshToken", 54, TimeUnit.HOURS);

}
