package com.orion.ops.framework.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.framework.mybatis.core.query.DataQuery;

import java.util.Collection;

/**
 * 通用 mapper
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:42
 */
public interface IMapper<T> extends BaseMapper<T> {

    /**
     * 获取 ValidateLambdaWrapper 对象
     *
     * @return 获取 wrapper
     */
    default LambdaQueryWrapper<T> wrapper() {
        return Conditions.wrapper();
    }

    /**
     * 获取 LambdaQueryWrapper 对象
     *
     * @return 获取 wrapper
     */
    default LambdaQueryWrapper<T> lambda() {
        return Conditions.lambda();
    }

    /**
     * 获取 DataQuery 对象
     *
     * @return DataQuery
     */
    default DataQuery<T> of() {
        return DataQuery.of(this);
    }

    /**
     * 获取 DataQuery 对象
     *
     * @param wrapper wrapper
     * @return DataQuery
     */
    default DataQuery<T> of(LambdaQueryWrapper<T> wrapper) {
        return DataQuery.of(this, wrapper);
    }

    /**
     * 批量插入
     *
     * @param entities entities
     * @return 是否成功
     */
    default boolean insertBatch(Collection<T> entities) {
        return Db.saveBatch(entities);
    }

    /**
     * 批量插入
     *
     * @param entities entities
     * @param size     size
     * @return 是否成功
     */
    default boolean insertBatch(Collection<T> entities, int size) {
        return Db.saveBatch(entities, size);
    }

    /**
     * 批量更新 (通过id)
     *
     * @param entities entities
     * @return 是否成功
     */
    default boolean updateBatch(Collection<T> entities) {
        return Db.updateBatchById(entities);
    }

    /**
     * 批量更新 (通过id)
     *
     * @param entities entities
     * @param size     size
     * @return 是否成功
     */
    default boolean updateBatch(Collection<T> entities, int size) {
        return Db.updateBatchById(entities, size);
    }

    /**
     * 插入或更新 (通过id)
     *
     * @param entity entity
     * @return 是否成功
     */
    default boolean insertOrUpdate(T entity) {
        return Db.saveOrUpdate(entity);
    }

    /**
     * 批量插入或更新 (通过id)
     *
     * @param entities entities
     * @return 是否成功
     */
    default boolean insertOrUpdateBatch(Collection<T> entities) {
        return Db.saveOrUpdateBatch(entities);
    }

    /**
     * 批量插入或更新 (通过id)
     *
     * @param entities entities
     * @param size     size
     * @return 是否成功
     */
    default boolean insertOrUpdateBatch(Collection<T> entities, int size) {
        return Db.saveOrUpdateBatch(entities, size);
    }

}
