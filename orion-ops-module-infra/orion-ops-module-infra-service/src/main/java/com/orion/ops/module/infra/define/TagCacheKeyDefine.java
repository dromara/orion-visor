package com.orion.ops.module.infra.define;

import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;
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
            .timeout(3, TimeUnit.DAYS)
            .build();

    CacheKeyDefine TAG_REL = new CacheKeyBuilder()
            .key("tag:rel:{}:{}")
            .desc("tag 引用 ${type} ${relId}")
            .type(TagCacheDTO.class)
            .timeout(3, TimeUnit.DAYS)
            .build();

}
