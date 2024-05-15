package com.orion.visor.module.infra.define.cache;

import com.alibaba.fastjson.JSONObject;
import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.visor.module.infra.entity.dto.DictKeyCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 字典配置项缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
public interface DictCacheKeyDefine {

    CacheKeyDefine DICT_KEY = new CacheKeyBuilder()
            .key("dict:keys:list")
            .desc("字典配置项")
            .type(DictKeyCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine DICT_SCHEMA = new CacheKeyBuilder()
            .key("dict:key-schema:{}")
            .desc("字典配置项 schema ${key}")
            .type(JSONObject.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine DICT_VALUE = new CacheKeyBuilder()
            .key("dict:values:{}")
            .desc("字典配置值 ${key}")
            .type(JSONObject.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
