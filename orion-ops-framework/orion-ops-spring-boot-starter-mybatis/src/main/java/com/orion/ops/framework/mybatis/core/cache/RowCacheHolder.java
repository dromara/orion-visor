package com.orion.ops.framework.mybatis.core.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orion.lang.define.collect.MultiHashMap;
import com.orion.lang.define.wrapper.Store;

import java.io.Serializable;

/**
 * 缓存行持有者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 14:21
 */
public class RowCacheHolder {

    private RowCacheHolder() {
    }

    /**
     * 缓存
     * <p>
     * key: mapperClass
     * value: id > row
     */
    private static final ThreadLocal<MultiHashMap<Class<? extends BaseMapper<?>>, Serializable, Store<?>>> HOLDER = ThreadLocal.withInitial(MultiHashMap::new);

    /**
     * 获取缓存
     *
     * @param mapperClass mapperClass
     * @param id          id
     * @param <T>         domain
     * @return cacheWrapper
     */
    @SuppressWarnings("unchecked")
    public static <T> Store<T> get(Class<? extends BaseMapper<?>> mapperClass, Serializable id) {
        return (Store<T>) HOLDER.get().get(mapperClass, id);
    }

    /**
     * 设置缓存
     *
     * @param mapperClass mapperClass
     * @param id          id
     * @param row         row
     * @param <T>         domainClass
     */
    public static <T> void set(Class<? extends BaseMapper<T>> mapperClass, Serializable id, T row) {
        HOLDER.get().put(mapperClass, id, new Store<>(row));
    }

    /**
     * 清空缓存
     */
    public static void remove() {
        HOLDER.remove();
    }

}
