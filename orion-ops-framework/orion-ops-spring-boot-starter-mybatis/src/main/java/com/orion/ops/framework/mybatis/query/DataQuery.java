package com.orion.ops.framework.mybatis.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.PageRequest;
import com.orion.lang.define.wrapper.Pager;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.convert.Converts;

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

    // FIXME mapstruct
    public <R> Optional<R> get(Class<R> c) {
        return Optional.ofNullable(dao.selectOne(wrapper))
                .map(s -> Converts.to(s, c));
    }

    public Stream<T> list() {
        return dao.selectList(wrapper).stream();
    }

    // FIXME mapstruct
    public <R> List<R> list(Class<R> c) {
        return Converts.toList(dao.selectList(wrapper), c);
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

    // FIXME mapstruct
    public <R> DataGrid<R> dataGrid(Class<R> c) {
        return this.dataGrid(t -> Converts.to(t, c));
    }

    public <R> DataGrid<R> dataGrid(Function<T, R> convert) {
        Valid.notNull(convert, "convert is null");
        Valid.notNull(page, "page is null");
        Valid.notNull(wrapper, "wrapper is null");
        Long count = dao.selectCount(wrapper);
        Pager<R> pager = new Pager<>(page);
        pager.setTotal(count.intValue());
        boolean next = pager.hasMoreData();
        if (next) {
            wrapper.last(pager.getSql());
            List<R> rows = dao.selectList(wrapper).stream()
                    .map(convert)
                    .collect(Collectors.toList());
            pager.setRows(rows);
        } else {
            pager.setRows(Lists.empty());
        }
        return DataGrid.of(pager);
    }

}
