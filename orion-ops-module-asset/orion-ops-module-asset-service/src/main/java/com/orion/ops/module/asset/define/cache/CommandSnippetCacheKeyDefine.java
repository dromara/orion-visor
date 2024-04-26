package com.orion.ops.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.ops.module.asset.entity.dto.CommandSnippetCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 命令片段缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
public interface CommandSnippetCacheKeyDefine {

    CacheKeyDefine SNIPPET = new CacheKeyBuilder()
            .key("command:snippet:list:{}")
            .desc("命令片段列表 ${userId}")
            .type(CommandSnippetCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
