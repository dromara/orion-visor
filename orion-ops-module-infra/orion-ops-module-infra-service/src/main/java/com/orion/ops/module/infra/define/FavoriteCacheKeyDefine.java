package com.orion.ops.module.infra.define;

import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;

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
            .timeout(8, TimeUnit.HOURS)
            .build();

}
