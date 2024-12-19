/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.mybatis.core.query.DataQuery;

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
        return this.insertBatch(entities, Const.BATCH_COUNT);
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
        return this.updateBatch(entities, Const.BATCH_COUNT);
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
        return this.insertOrUpdateBatch(entities, Const.BATCH_COUNT);
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
