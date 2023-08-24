package com.orion.ops.framework.mybatis.core.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orion.lang.define.collect.MultiHashMap;
import com.orion.lang.define.wrapper.Ref;

import java.io.Serializable;

/**
 * 缓存行持有者
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 14:21
 */
public class CacheHolder {

    private CacheHolder() {
    }

    /**
     * 缓存
     * <p>
     * key: mapper
     * value: id > row
     */
    private static final ThreadLocal<MultiHashMap<BaseMapper<?>, Serializable, Ref<?>>> HOLDER = ThreadLocal.withInitial(MultiHashMap::new);

    /**
     * 获取缓存
     *
     * @param mapper mapper
     * @param id     id
     * @param <T>    domain
     * @return cacheWrapper
     */
    @SuppressWarnings("unchecked")
    public static <T> Ref<T> get(BaseMapper<?> mapper, Serializable id) {
        return (Ref<T>) HOLDER.get().get(mapper, id);
    }

    /**
     * 设置缓存
     *
     * @param mapper mapper
     * @param id     id
     * @param row    row
     * @param <T>    domainClass
     */
    public static <T> void set(BaseMapper<T> mapper, Serializable id, T row) {
        HOLDER.get().put(mapper, id, new Ref<>(row));
    }

    /**
     * 清空缓存
     */
    public static void remove() {
        HOLDER.remove();
    }

}
