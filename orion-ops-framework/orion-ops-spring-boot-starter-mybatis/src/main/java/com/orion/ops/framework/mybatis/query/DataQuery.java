package com.orion.ops.framework.mybatis.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.PageRequest;
import com.orion.lang.define.wrapper.Pager;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;

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

    private LambdaQueryWrapper<T> wrapper;

    private DataQuery(BaseMapper<T> dao) {
        this.dao = dao;
    }

    public static <T> DataQuery<T> of(BaseMapper<T> dao) {
        Valid.notNull(dao, "dao is null");
        return new DataQuery<>(dao);
    }

    public DataQuery<T> page(PageRequest page) {
        this.page = Valid.notNull(page, "page is null");
        return this;
    }

    public DataQuery<T> wrapper(LambdaQueryWrapper<T> wrapper) {
        this.wrapper = Valid.notNull(wrapper, "wrapper is null");
        return this;
    }

    public Optional<T> get() {
        return Optional.ofNullable(dao.selectOne(wrapper));
    }

    public <R> Optional<R> get(Function<T, R> mapper) {
        Valid.notNull(mapper, "convert function is null");
        return Optional.ofNullable(dao.selectOne(wrapper))
                .map(mapper);
    }

    public Stream<T> list() {
        return dao.selectList(wrapper).stream();
    }

    public <R> List<R> list(Function<T, R> mapper) {
        Valid.notNull(mapper, "convert function is null");
        return Lists.map(dao.selectList(wrapper), mapper);
    }

    public Long count() {
        return dao.selectCount(wrapper);
    }

    public boolean present() {
        return dao.selectCount(wrapper) > 0;
    }

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
            wrapper.last(pager.getSql());
            List<R> rows = dao.selectList(wrapper).stream()
                    .map(mapper)
                    .collect(Collectors.toList());
            pager.setRows(rows);
        } else {
            pager.setRows(Lists.empty());
        }
        return DataGrid.of(pager);
    }

}
