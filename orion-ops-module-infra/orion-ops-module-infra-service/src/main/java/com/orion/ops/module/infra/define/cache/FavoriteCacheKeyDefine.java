package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * 收藏服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/31 11:48
 */
public interface FavoriteCacheKeyDefine {

    CacheKeyDefine FAVORITE = new CacheKeyBuilder()
            .key("favorite:{}:{}")
            .desc("收藏信息 ${type} ${userId}")
            .type(Long.class)
            .struct(RedisCacheStruct.LIST)
            .timeout(3, TimeUnit.DAYS)
            .build();

}
