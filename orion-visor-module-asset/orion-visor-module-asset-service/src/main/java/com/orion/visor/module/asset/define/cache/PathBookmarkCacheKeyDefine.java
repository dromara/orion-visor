package com.orion.visor.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.visor.module.asset.entity.dto.PathBookmarkCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 路径标签缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
public interface PathBookmarkCacheKeyDefine {

    CacheKeyDefine PATH_BOOKMARK = new CacheKeyBuilder()
            .key("path:bookmark:list:{}")
            .desc("路径标签列表 ${userId}")
            .type(PathBookmarkCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
