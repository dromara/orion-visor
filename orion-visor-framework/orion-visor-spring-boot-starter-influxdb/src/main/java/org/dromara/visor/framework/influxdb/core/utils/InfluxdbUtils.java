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
package org.dromara.visor.framework.influxdb.core.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.entity.chart.TimeChartSeries;
import org.dromara.visor.framework.influxdb.core.query.FluxQueryBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * influxdb 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/10 20:47
 */
public class InfluxdbUtils {

    private static final String FIELD_KEY = "_field";

    private static final List<String> SKIP_EXTRA_KEY = Lists.of("result", "table", "_measurement", "_start", "_stop", "_time", "_value");

    private static InfluxDBClient client;

    private static String bucket;

    private InfluxdbUtils() {
    }

    /**
     * 写入指标
     *
     * @param points points
     */
    public static void writePoints(List<Point> points) {
        try (WriteApi api = client.makeWriteApi()) {
            // 写入指标
            api.writePoints(points);
        }
    }

    /**
     * 查询数据点
     *
     * @param query query
     * @return points
     */
    public static List<FluxTable> queryTable(String query) {
        return client.getQueryApi().query(query);
    }

    /**
     * 查询数据点
     *
     * @param query query
     * @return points
     */
    public static FluxTable querySingleTable(String query) {
        return Lists.first(queryTable(query));
    }

    /**
     * 查询时序系列
     *
     * @param query query
     * @return points
     */
    public static List<TimeChartSeries> querySeries(String query) {
        return toSeries(queryTable(query));
    }

    /**
     * 查询时序系列
     *
     * @param query query
     * @return points
     */
    public static TimeChartSeries querySingleSeries(String query) {
        return toSeries(querySingleTable(query));
    }

    /**
     * 转为时序系列
     *
     * @param table table
     * @return series
     */
    public static TimeChartSeries toSeries(FluxTable table) {
        // 数据
        Map<String, Object> tags = new HashMap<>();
        List<List<Object>> dataList = new ArrayList<>();
        for (FluxRecord record : table.getRecords()) {
            Instant time = record.getTime();
            if (time == null) {
                continue;
            }
            // 设置数据
            List<Object> data = new ArrayList<>(2);
            data.add(time.toEpochMilli());
            data.add(record.getValue());
            dataList.add(data);
            // 设置额外值
            record.getValues().forEach((k, v) -> {
                if (SKIP_EXTRA_KEY.contains(k)) {
                    return;
                }
                tags.put(k, v);
            });
        }
        // 设置 field
        tags.put(Const.FIELD, tags.get(FIELD_KEY));
        tags.remove(FIELD_KEY);
        // 创建 series
        return TimeChartSeries.builder()
                .data(dataList)
                .tags(tags)
                .build();
    }

    /**
     * 转为时序系列
     *
     * @param tables tables
     * @return series
     */
    public static List<TimeChartSeries> toSeries(List<FluxTable> tables) {
        return tables.stream()
                .map(InfluxdbUtils::toSeries)
                .collect(Collectors.toList());
    }

    /**
     * 获取查询构建器
     *
     * @return builder
     */
    public static FluxQueryBuilder query() {
        return FluxQueryBuilder.from(bucket);
    }

    public static void setInfluxClient(String bucket, InfluxDBClient client) {
        if (InfluxdbUtils.client != null) {
            // unmodified
            throw Exceptions.state();
        }
        InfluxdbUtils.client = client;
        InfluxdbUtils.bucket = bucket;
    }

}
