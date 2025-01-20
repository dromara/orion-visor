/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.framework.mybatis.core.query;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.define.wrapper.IPageRequest;
import cn.orionsec.kit.lang.define.wrapper.PageRequest;
import cn.orionsec.kit.lang.define.wrapper.Pager;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.reflect.Classes;
import cn.orionsec.kit.spring.SpringHolder;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.utils.SqlUtils;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据查询器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:52
 */
public class DataQuery<T> {

    private final BaseMapper<T> dao;

    private PageRequest page;

    private Wrapper<T> wrapper;

    private DataQuery(BaseMapper<T> dao) {
        this.dao = dao;
    }

    private DataQuery(BaseMapper<T> dao, Wrapper<T> wrapper) {
        this.dao = dao;
        this.wrapper = wrapper;
    }

    /**
     * 通过实体类直接获取 DataQuery
     *
     * @param entityClass entityClass
     * @param <T>         entityClass
     * @return DataQuery
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseDO> DataQuery<T> create(Class<T> entityClass) {
        TableInfo table = Valid.notNull(TableInfoHelper.getTableInfo(entityClass), "notfound mapper class");
        Class<BaseMapper<T>> mapperClass = (Class<BaseMapper<T>>) Classes.loadClass(table.getCurrentNamespace());
        return new DataQuery<T>(SpringHolder.getBean(mapperClass));
    }

    public static <T> DataQuery<T> of(BaseMapper<T> dao) {
        Valid.notNull(dao, "dao is null");
        return new DataQuery<>(dao);
    }

    public static <T> DataQuery<T> of(BaseMapper<T> dao, Wrapper<T> wrapper) {
        Valid.notNull(dao, "dao is null");
        return new DataQuery<>(dao, wrapper);
    }

    public DataQuery<T> page(org.dromara.visor.common.entity.PageRequest page) {
        org.dromara.visor.common.entity.PageRequest pr = Valid.notNull(page, "page is null");
        this.page = new PageRequest(pr.getPage(), pr.getLimit());
        return this;
    }

    public DataQuery<T> page(int page, int limit) {
        this.page = new PageRequest(page, limit);
        return this;
    }

    public DataQuery<T> wrapper(Wrapper<T> wrapper) {
        this.wrapper = Valid.notNull(wrapper, "wrapper is null");
        return this;
    }

    // -------------------- condition --------------------

    public ThenLambdaWrapper<T> createWrapper() {
        return this.createWrapper(false);
    }

    public ThenLambdaWrapper<T> createValidateWrapper() {
        return this.createWrapper(true);
    }

    public ThenLambdaWrapper<T> createWrapper(boolean validate) {
        ThenLambdaWrapper<T> then = validate
                ? new ThenValidateLambdaWrapper<>(this)
                : new ThenLambdaWrapper<>(this);
        this.wrapper = then;
        return then;
    }

    public DataQuery<T> limit(IPageRequest page) {
        return this.last(Pager.of(page).getSql());
    }

    public DataQuery<T> limit(int limit) {
        return this.last(SqlUtils.limit(limit));
    }

    public DataQuery<T> limit(int offset, int limit) {
        return this.last(SqlUtils.limit(offset, limit));
    }

    public DataQuery<T> only() {
        return this.last(Const.LIMIT_1);
    }

    public DataQuery<T> last(String lastSql) {
        if (wrapper instanceof Join) {
            ((Join<?>) wrapper).last(lastSql);
            return this;
        } else {
            throw Exceptions.argument("wrapper not implements Join");
        }
    }

    // -------------------- id --------------------

    public T get(Serializable id) {
        Valid.notNull(id, "id is null");
        return dao.selectById(id);
    }

    public <R> R get(Serializable id, Function<T, R> mapper) {
        Valid.notNull(id, "id is null");
        Valid.notNull(mapper, "convert function is null");
        return Objects1.map(dao.selectById(id), mapper);
    }

    public Optional<T> optional(Serializable id) {
        Valid.notNull(id, "id is null");
        return Optional.ofNullable(dao.selectById(id));
    }

    // -------------------- one --------------------

    public T getOne() {
        return this.only().get();
    }

    public <R> R getOne(Function<T, R> mapper) {
        return this.only().get(mapper);
    }

    public Optional<T> optionalOne() {
        return this.only().optional();
    }

    // -------------------- get --------------------

    public T get() {
        return dao.selectOne(wrapper);
    }

    public <R> R get(Function<T, R> mapper) {
        Valid.notNull(mapper, "convert function is null");
        return Objects1.map(dao.selectOne(wrapper), mapper);
    }

    public Optional<T> optional() {
        return Optional.ofNullable(dao.selectOne(wrapper));
    }

    // -------------------- list --------------------

    public List<T> list() {
        return dao.selectList(wrapper);
    }

    public <R> List<R> list(Function<T, R> mapper) {
        Valid.notNull(mapper, "convert function is null");
        return Lists.map(dao.selectList(wrapper), mapper);
    }

    public Stream<T> stream() {
        return dao.selectList(wrapper).stream();
    }

    // -------------------- statistic --------------------

    public Long count() {
        return dao.selectCount(wrapper);
    }

    public Long countMax(Number max) {
        Long count = dao.selectCount(wrapper);
        if (max == null) {
            return count;
        }
        long maxValue = max.longValue();
        if (maxValue <= 0L) {
            return count;
        }
        return Math.min(count, maxValue);
    }

    public boolean absent() {
        return dao.selectCount(wrapper) == 0;
    }

    public boolean present() {
        return dao.selectCount(wrapper) > 0;
    }

    // -------------------- data grid --------------------

    public DataGrid<T> dataGrid() {
        return this.dataGrid(this.wrapper, Function.identity());
    }

    public DataGrid<T> dataGrid(Wrapper<T> countWrapper) {
        return this.dataGrid(countWrapper, Function.identity());
    }

    public <R> DataGrid<R> dataGrid(Function<T, R> mapper) {
        return this.dataGrid(this.wrapper, mapper);
    }

    public <R> DataGrid<R> dataGrid(Wrapper<T> countWrapper, Function<T, R> mapper) {
        Valid.notNull(page, "page is null");
        Valid.notNull(wrapper, "wrapper is null");
        Valid.notNull(countWrapper, "count wrapper is null");
        Valid.notNull(mapper, "convert function is null");
        Long count = dao.selectCount(countWrapper);
        Pager<R> pager = new Pager<>(page);
        pager.setTotal(count.intValue());
        boolean next = pager.hasMoreData();
        if (next) {
            this.last(pager.getSql());
            List<R> rows = dao.selectList(wrapper)
                    .stream()
                    .map(mapper)
                    .collect(Collectors.toList());
            pager.setRows(rows);
        } else {
            pager.setRows(Lists.empty());
        }
        return DataGrid.of(pager);
    }

}
