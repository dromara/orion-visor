package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * tips 服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 17:58
 */
public interface TipsCacheKeyDefine {

    CacheKeyDefine TIPS = new CacheKeyBuilder()
            .key("user:tips:{}")
            .desc("user:tips ${userId} 90天不会重复提示")
            .type(String.class)
            .struct(RedisCacheStruct.LIST)
            .timeout(90, TimeUnit.DAYS)
            .build();

}
