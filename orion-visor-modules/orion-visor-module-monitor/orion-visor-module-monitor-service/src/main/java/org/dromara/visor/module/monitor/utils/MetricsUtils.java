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
package org.dromara.visor.module.monitor.utils;

import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSONObject;
import com.influxdb.client.write.Point;
import org.dromara.visor.module.monitor.enums.MeasurementFieldEnum;

/**
 * 指标值工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/11 22:46
 */
public class MetricsUtils {

    private MetricsUtils() {
    }

    /**
     * 设置值
     *
     * @param type   type
     * @param values values
     * @return point
     */
    public static Point createPoint(String type, JSONObject values) {
        // 创建数据点
        Point point = Point.measurement(type);
        // 设置数据值
        for (String field : values.keySet()) {
            // 数据类型
            Class<?> dataType = MeasurementFieldEnum.getMetricsValueType(type, field);
            if (dataType == null) {
                continue;
            }
            // 过滤数据
            String str = values.getString(field);
            if (Strings.isBlank(str)) {
                continue;
            }
            // 转换数据类型
            if (dataType == byte.class || dataType == short.class || dataType == int.class || dataType == long.class) {
                point.addField(field, values.getLongValue(field));
            } else if (dataType == float.class || dataType == double.class) {
                point.addField(field, values.getDoubleValue(field));
            } else if (dataType == boolean.class) {
                point.addField(field, values.getBoolean(field));
            } else if (dataType == String.class) {
                point.addField(field, values.getString(field));
            }
        }
        return point;
    }

}
