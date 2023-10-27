package com.orion.ops.framework.mybatis.core.generator.template;

import java.util.concurrent.TimeUnit;

/**
 * 后端代码缓存模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:14
 */
public class CacheTemplate extends ServerTemplate {

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
