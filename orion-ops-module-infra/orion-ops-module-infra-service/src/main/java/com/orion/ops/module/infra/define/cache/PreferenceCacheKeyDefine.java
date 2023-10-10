package com.orion.ops.module.infra.define.cache;

import com.alibaba.fastjson.JSONObject;
import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;

import java.util.concurrent.TimeUnit;

/**
 * 用户偏好缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
public interface PreferenceCacheKeyDefine {

    CacheKeyDefine PREFERENCE = new CacheKeyBuilder()
            .key("user:preference:{}:{}")
            .desc("用户偏好 ${userId} ${type}")
            .type(JSONObject.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

}
