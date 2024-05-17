package com.orion.visor.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.lang.define.wrapper.Ref;

import java.util.concurrent.TimeUnit;

/**
 * 用户偏好缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
public interface PreferenceCacheKeyDefine {

    CacheKeyDefine PREFERENCE = new CacheKeyBuilder()
            .key("user:preference:{}:{}")
            .desc("用户偏好 ${userId} ${type}")
            .type(Ref.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
