package com.orion.ops.module.asset.define.cache;

import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.ops.module.asset.entity.dto.HostIdentityCacheDTO;
import com.orion.ops.module.asset.entity.dto.HostKeyCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 主机服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 13:54
 */
public interface HostCacheKeyDefine {

    CacheKeyDefine HOST_KEY = new CacheKeyBuilder()
            .key("host:key:list")
            .desc("主机秘钥列表")
            .type(HostKeyCacheDTO.class)
            .timeout(1, TimeUnit.HOURS)
            .build();

    CacheKeyDefine HOST_IDENTITY = new CacheKeyBuilder()
            .key("host:identity:list")
            .desc("主机身份列表")
            .type(HostIdentityCacheDTO.class)
            .timeout(1, TimeUnit.HOURS)
            .build();

}
