package com.orion.ops.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.ops.module.asset.entity.dto.ExecTemplateCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 执行模板缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
public interface ExecTemplateCacheKeyDefine {

    CacheKeyDefine EXEC_TEMPLATE = new CacheKeyBuilder()
            .key("exec:template:list")
            .desc("执行模板列表")
            .type(ExecTemplateCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(1, TimeUnit.DAYS)
            .build();

}
