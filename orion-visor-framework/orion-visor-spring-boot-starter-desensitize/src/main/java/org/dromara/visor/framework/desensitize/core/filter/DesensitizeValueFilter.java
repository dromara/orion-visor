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
package org.dromara.visor.framework.desensitize.core.filter;

import cn.orionsec.kit.lang.utils.Desensitizes;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.reflect.Annotations;
import cn.orionsec.kit.lang.utils.reflect.Fields;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.desensitize.core.annotation.Desensitize;
import org.dromara.visor.framework.desensitize.core.annotation.DesensitizeObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fastjson 脱敏序列化器
 * <p>
 * 用于全局日志打印
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 12:19
 */
public class DesensitizeValueFilter implements ValueFilter {

    private static final Map<String, Map<String, Desensitize>> DESENSITIZE_FIELDS = new HashMap<>();

    @Override
    public Object process(Object object, String name, Object value) {
        if (object == null || value == null) {
            return value;
        }
        Desensitize config = this.doDesensitizeField(object, name);
        // 无需脱敏
        if (config == null) {
            return value;
        }
        if (config.toEmpty()) {
            // 设置为空
            return Const.EMPTY;
        } else {
            // 脱敏
            return Desensitizes.mix(Objects1.toString(value), config.keepStart(), config.keepEnd(), config.replacer());
        }
    }

    /**
     * 是否要执行脱敏
     *
     * @param object object
     * @param name   name
     * @return 是否执行
     */
    private Desensitize doDesensitizeField(Object object, String name) {
        // 查询缓存
        String className = object.getClass().toString();
        Map<String, Desensitize> fields = DESENSITIZE_FIELDS.get(className);
        if (fields == null) {
            // 查询脱敏配置
            fields = this.initClassDesensitize(object);
            DESENSITIZE_FIELDS.put(className, fields);
        }
        // 无需脱敏
        if (fields.isEmpty()) {
            return null;
        }
        return fields.get(name);
    }

    /**
     * 初始化脱敏配置
     *
     * @param object object
     * @return config
     */
    private Map<String, Desensitize> initClassDesensitize(Object object) {
        Class<?> dataClass = object.getClass();
        // 检查是否为脱敏对象
        DesensitizeObject has = Annotations.getAnnotation(dataClass, DesensitizeObject.class);
        if (has == null) {
            return Maps.empty();
        }
        Map<String, Desensitize> config = new HashMap<>();
        // 获取对象字段
        List<Field> fields = Fields.getFields(dataClass);
        // 查询脱敏配置
        for (Field field : fields) {
            // 脱敏注解
            Desensitize desensitize = Annotations.getAnnotation(field, Desensitize.class);
            if (desensitize == null) {
                continue;
            }
            // json 注解
            JSONField jsonField = Annotations.getAnnotation(field, JSONField.class);
            String fieldName = field.getName();
            String jsonFieldName;
            if (jsonField != null && !Strings.isBlank(jsonFieldName = jsonField.name())) {
                fieldName = jsonFieldName;
            }
            config.put(fieldName, desensitize);
        }
        return config;
    }

}
