package com.orion.ops.framework.mybatis.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orion.lang.define.wrapper.Store;
import com.orion.lang.utils.Valid;

import java.io.Serializable;

/**
 * 缓存查询器
 * <p>
 * 查询会存入缓存
 * <p>
 * TODO test
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 13:05
 */
public class CacheQuery<T> {

    private final BaseMapper<T> dao;

    private Serializable id;

    private CacheQuery(BaseMapper<T> dao, Serializable id) {
        this.dao = dao;
        this.id = id;
    }

    public static <T> CacheQuery<T> of(BaseMapper<T> dao) {
        Valid.notNull(dao, "dao is null");
        return new CacheQuery<>(dao, null);
    }

    public static <T> CacheQuery<T> of(BaseMapper<T> dao, Serializable id) {
        Valid.notNull(dao, "dao is null");
        Valid.notNull(id, "id is null");
        return new CacheQuery<>(dao, id);
    }

    /**
     * 设置 id
     *
     * @param id id
     * @return this
     */
    public CacheQuery<T> id(Serializable id) {
        this.id = id;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R> R get(Class<R> c) {
        T row = this.get();
        // TODO FIXME mapstruct
        return (R) row;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        Class<? extends BaseMapper<T>> mapperClass = (Class<? extends BaseMapper<T>>) dao.getClass();
        // 从缓存中获取
        Store<T> store = RowCacheHolder.get(mapperClass, id);
        // 设置过缓存
        if (store != null) {
            return store.get();
        }
        // 查询
        T row = dao.selectById(id);
        // 设置缓存
        RowCacheHolder.set(mapperClass, id, row);
        return row;
    }

}
