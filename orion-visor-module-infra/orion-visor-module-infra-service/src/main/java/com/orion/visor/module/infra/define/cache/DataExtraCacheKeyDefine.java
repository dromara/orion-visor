package com.orion.visor.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * 数据拓展缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
public interface DataExtraCacheKeyDefine {

    CacheKeyDefine DATA_EXTRA = new CacheKeyBuilder()
            .key("data:extra:{}:{}:{}")
            .desc("数据拓展信息 ${userId} ${type} ${item}")
            .type(String.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
