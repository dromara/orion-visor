package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * 数据别名缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
public interface DataAliasCacheKeyDefine {

    CacheKeyDefine DATA_ALIAS = new CacheKeyBuilder()
            .key("data:alias:{}:{}")
            .desc("数据别名 ${userId} ${type}")
            .type(String.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(1, TimeUnit.DAYS)
            .build();

}
