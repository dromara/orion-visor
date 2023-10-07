package com.orion.ops.framework.mybatis.core.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.IPageRequest;
import com.orion.lang.define.wrapper.PageRequest;
import com.orion.lang.define.wrapper.Pager;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.reflect.Classes;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import com.orion.spring.SpringHolder;

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

    private IPageRequest page;

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

    public DataQuery<T> page(IPageRequest page) {
        this.page = Valid.notNull(page, "page is null");
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

    public boolean absent() {
        return dao.selectCount(wrapper) == 0;
    }

    public boolean present() {
        return dao.selectCount(wrapper) > 0;
    }

    // -------------------- data grid --------------------

    public DataGrid<T> dataGrid() {
        return this.dataGrid(Function.identity());
    }

    public <R> DataGrid<R> dataGrid(Function<T, R> mapper) {
        Valid.notNull(mapper, "convert function is null");
        Valid.notNull(page, "page is null");
        Valid.notNull(wrapper, "wrapper is null");
        Long count = dao.selectCount(wrapper);
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
