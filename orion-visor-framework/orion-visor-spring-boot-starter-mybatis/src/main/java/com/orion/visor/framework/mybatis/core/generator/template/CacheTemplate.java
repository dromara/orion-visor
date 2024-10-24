/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.framework.mybatis.core.generator.template;

import java.util.concurrent.TimeUnit;

/**
 * 后端代码缓存模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:14
 */
public class CacheTemplate extends Template {

    public CacheTemplate(Table table) {
        this(table, table.cacheKey, table.cacheDesc);
    }

    public CacheTemplate(Table table, String key) {
        this(table, key, table.cacheDesc);
    }

    public CacheTemplate(Table table, String key, String desc) {
        super(table);
        table.enableCache = true;
        table.cacheExpireTime = 1;
        table.cacheExpireUnit = TimeUnit.HOURS;
        table.cacheKey = key;
        table.cacheDesc = desc;
    }

    /**
     * 设置缓存 key
     *
     * @param key key
     * @return this
     */
    public CacheTemplate key(String key) {
        table.cacheKey = key;
        return this;
    }

    /**
     * 设置缓存 key
     *
     * @param key key
     * @return this
     */
    public CacheTemplate key(String key, String desc) {
        table.cacheKey = key;
        table.cacheDesc = desc;
        return this;
    }

    /**
     * 设置缓存描述
     *
     * @param desc desc
     * @return this
     */
    public CacheTemplate desc(String desc) {
        table.cacheDesc = desc;
        return this;
    }

    /**
     * 设置缓存过期时间
     *
     * @param time time
     * @return this
     */
    public CacheTemplate expireTime(Integer time) {
        table.cacheExpired = true;
        table.cacheExpireTime = time;
        return this;
    }

    /**
     * 设置缓存过期时间单位
     *
     * @param unit unit
     * @return this
     */
    public CacheTemplate expireUnit(TimeUnit unit) {
        table.cacheExpired = true;
        table.cacheExpireUnit = unit;
        return this;
    }

    /**
     * 设置缓存过期时间
     *
     * @param time time
     * @param unit unit
     * @return this
     */
    public CacheTemplate expire(Integer time, TimeUnit unit) {
        table.cacheExpired = true;
        table.cacheExpireTime = time;
        table.cacheExpireUnit = unit;
        return this;
    }

}
