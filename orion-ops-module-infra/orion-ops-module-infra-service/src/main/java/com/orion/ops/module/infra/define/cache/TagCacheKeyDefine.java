package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.ops.module.infra.entity.dto.TagCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * tag 服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/5 15:36
 */
public interface TagCacheKeyDefine {

    CacheKeyDefine TAG_NAME = new CacheKeyBuilder()
            .key("tag:name:{}")
            .desc("tag 名称 ${type}")
            .type(TagCacheDTO.class)
            .struct(RedisCacheStruct.LIST)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine TAG_REL = new CacheKeyBuilder()
            .key("tag:rel:{}:{}")
            .desc("tag 引用 ${type} ${relId}")
            .type(TagCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
