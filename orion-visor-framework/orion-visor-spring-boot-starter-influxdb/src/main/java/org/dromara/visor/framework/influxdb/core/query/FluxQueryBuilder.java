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
package org.dromara.visor.framework.influxdb.core.query;

import cn.orionsec.kit.lang.utils.collect.Collections;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import org.dromara.visor.common.constant.Const;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * flux 查询构建器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/3 16:08
 */
public class FluxQueryBuilder {

    private final StringBuilder query;

    private boolean hasFilter;

    private boolean pretty;

    private FluxQueryBuilder(String bucket) {
        this.query = new StringBuilder();
        this.query.append(String.format("from(bucket: \"%s\")", bucket));
    }

    /**
     * 创建构建器
     *
     * @param bucket bucket
     * @return builder
     */
    public static FluxQueryBuilder from(String bucket) {
        return new FluxQueryBuilder(bucket);
    }

    /**
     * 时间范围
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return this
     */
    public FluxQueryBuilder range(long start, long end) {
        query.append(String.format(" |> range(start: %s, stop: %s)", Instant.ofEpochMilli(start), Instant.ofEpochMilli(end)));
        return this;
    }

    /**
     * 时间范围
     *
     * @param range range
     * @return this
     */
    public FluxQueryBuilder range(String range) {
        query.append(String.format(" |> range(start: %s)", range));
        return this;
    }

    /**
     * 过滤 measurement
     *
     * @param measurement measurement
     * @return this
     */
    public FluxQueryBuilder measurement(String measurement) {
        this.appendFilter(String.format("r[\"_measurement\"] == \"%s\"", measurement));
        this.closeFilter();
        return this;
    }

    /**
     * 过滤单个 field
     *
     * @param field field
     * @return this
     */
    public FluxQueryBuilder field(String field) {
        this.appendFilter(String.format("r[\"_field\"] == \"%s\"", field));
        this.closeFilter();
        return this;
    }

    /**
     * 过滤多个 field
     *
     * @param fields fields
     * @return this
     */
    public FluxQueryBuilder fields(Collection<String> fields) {
        if (Collections.isEmpty(fields)) {
            return this;
        }
        List<String> conditions = new ArrayList<>();
        for (String field : fields) {
            conditions.add(String.format("r[\"_field\"] == \"%s\"", field));
        }
        this.appendFilter(String.join(" or ", conditions));
        this.closeFilter();
        return this;
    }

    /**
     * 过滤 tag key
     *
     * @param value value
     * @return this
     */
    public FluxQueryBuilder key(String value) {
        return this.tag(Const.KEY, value);
    }

    /**
     * 过滤 tag key
     *
     * @param values values
     * @return this
     */
    public FluxQueryBuilder key(Collection<String> values) {
        return this.tag(Const.KEY, values);
    }

    /**
     * 过滤 tag name
     *
     * @param value value
     * @return this
     */
    public FluxQueryBuilder name(String value) {
        return this.tag(Const.NAME, value);
    }

    /**
     * 过滤 tag name
     *
     * @param values values
     * @return this
     */
    public FluxQueryBuilder name(Collection<String> values) {
        return this.tag(Const.NAME, values);
    }

    /**
     * 过滤 tag
     *
     * @return this
     */
    public FluxQueryBuilder tag(String key, String value) {
        this.appendFilter(String.format("r[\"%s\"] == \"%s\"", key, value));
        this.closeFilter();
        return this;
    }

    /**
     * 过滤 tag
     *
     * @param key    key
     * @param values values
     * @return this
     */
    public FluxQueryBuilder tag(String key, Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return this;
        }
        if (values.size() == 1) {
            return this.tag(key, Collections.first(values));
        }
        // 使用 or 拼接
        Collection<String> conditions = values.stream()
                .map(value -> String.format("r[\"%s\"] == \"%s\"", key, value))
                .collect(Collectors.toList());
        this.appendFilter(String.join(" or ", conditions));
        this.closeFilter();
        return this;
    }

    /**
     * 过滤 tag
     *
     * @param tags tags
     * @return this
     */
    public FluxQueryBuilder tags(Map<String, ? extends Collection<String>> tags) {
        if (Maps.isEmpty(tags)) {
            return this;
        }
        for (Map.Entry<String, ? extends Collection<String>> entry : tags.entrySet()) {
            this.tag(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 聚合窗口
     */
    public FluxQueryBuilder aggregateWindow(String every, String fn) {
        query.append(String.format(" |> aggregateWindow(every: %s, fn: %s)", every, fn));
        return this;
    }

    /**
     * 聚合窗口
     */
    public FluxQueryBuilder aggregateWindow(String every, String fn, boolean createEmpty) {
        query.append(String.format(" |> aggregateWindow(every: %s, fn: %s, createEmpty: %b)", every, fn, createEmpty));
        return this;
    }

    /**
     * 排序
     *
     * @param columns columns
     * @return this
     */
    public FluxQueryBuilder sort(List<String> columns) {
        StringBuilder cols = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            cols.append("\"").append(columns.get(i)).append("\"");
            if (i < columns.size() - 1) cols.append(", ");
        }
        query.append(String.format(" |> sort(columns: [%s])", cols));
        return this;
    }

    /**
     * 降序
     *
     * @param column column
     * @return this
     */
    public FluxQueryBuilder sortDesc(String column) {
        return this.sort(Lists.singleton("-" + column));
    }

    /**
     * 升序
     *
     * @param column column
     * @return this
     */
    public FluxQueryBuilder sortAsc(String column) {
        return this.sort(Lists.singleton(column));
    }

    /**
     * 限制条数
     *
     * @param n limit
     * @return this
     */
    public FluxQueryBuilder limit(int n) {
        query.append(String.format(" |> limit(n: %d)", n));
        return this;
    }

    /**
     * 基础过滤拼接
     */
    private void appendFilter(String condition) {
        if (!hasFilter) {
            query.append(" |> filter(fn: (r) => ");
            this.hasFilter = true;
        } else {
            query.append(" and ");
        }
        query.append(condition);
    }

    /**
     * 结束 filter 并闭合括号
     */
    private void closeFilter() {
        if (hasFilter) {
            query.append(")");
            this.hasFilter = false;
        }
    }

    /**
     * 设置美观输出
     *
     * @return this
     */
    public FluxQueryBuilder pretty() {
        this.pretty = true;
        return this;
    }

    /**
     * 构建查询
     */
    public String build() {
        if (this.pretty) {
            return query.toString().replaceAll("\\|>", "\n  |>");
        } else {
            return query.toString();
        }
    }

    @Override
    public String toString() {
        return this.build();
    }

}
