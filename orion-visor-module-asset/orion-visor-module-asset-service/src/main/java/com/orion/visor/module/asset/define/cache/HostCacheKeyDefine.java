package com.orion.visor.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.visor.module.asset.entity.dto.HostCacheDTO;
import com.orion.visor.module.asset.entity.dto.HostIdentityCacheDTO;
import com.orion.visor.module.asset.entity.dto.HostKeyCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 主机服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 13:54
 */
public interface HostCacheKeyDefine {

    CacheKeyDefine HOST_INFO = new CacheKeyBuilder()
            .key("host:info:list")
            .desc("主机列表")
            .type(HostCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine HOST_KEY = new CacheKeyBuilder()
            .key("host:key:list")
            .desc("主机密钥列表")
            .type(HostKeyCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine HOST_IDENTITY = new CacheKeyBuilder()
            .key("host:identity:list")
            .desc("主机身份列表")
            .type(HostIdentityCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
