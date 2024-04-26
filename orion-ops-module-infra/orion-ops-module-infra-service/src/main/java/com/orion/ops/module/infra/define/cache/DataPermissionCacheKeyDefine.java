package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * 数据权限缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/21 11:29
 */
public interface DataPermissionCacheKeyDefine {

    CacheKeyDefine DATA_PERMISSION_USER = new CacheKeyBuilder()
            .key("data:perm-user:{}:{}")
            .desc("用户所有数据权限 ${type} ${userId}")
            .type(Long.class)
            .struct(RedisCacheStruct.LIST)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
