package com.orion.visor.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.lang.define.wrapper.Ref;

import java.util.concurrent.TimeUnit;

/**
 * 系统配置缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
public interface SystemSettingKeyDefine {

    CacheKeyDefine SETTING = new CacheKeyBuilder()
            .key("system:setting:{}")
            .desc("系统设置 ${type}")
            .type(Ref.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
