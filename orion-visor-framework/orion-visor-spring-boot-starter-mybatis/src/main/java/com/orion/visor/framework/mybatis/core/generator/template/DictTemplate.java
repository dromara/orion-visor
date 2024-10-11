/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.framework.mybatis.core.generator.template;

import com.orion.lang.utils.collect.Lists;
import com.orion.visor.framework.common.constant.Const;

import java.util.LinkedHashMap;

/**
 * 字典配置模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:14
 */
public class DictTemplate extends Template {

    //      // $comment
    //      export const $field = {
    //        // labels[0]
    //        fields[0]: 'values[0]',
    //        // labels[1]
    //        fields[0]: 'values[1]',
    //      };
    //
    //      // $comment 字典项
    //      export const $keyField = '$keyName';

    private final DictMeta dictMeta;

    public DictTemplate(Table table, String keyName, String variable) {
        this(table, keyName, variable, null);
    }

    public DictTemplate(Table table, String keyName, String variable, String field) {
        super(table);
        this.dictMeta = new DictMeta(keyName, variable, field);
        table.dictList.add(dictMeta);
    }

    /**
     * 设置字典配置名称
     *
     * @param keyName keyName
     * @return this
     */
    public DictTemplate keyName(String keyName) {
        dictMeta.keyName = keyName;
        return this;
    }

    /**
     * 设置字段名称
     *
     * @param field field
     * @return this
     */
    public DictTemplate field(String field) {
        dictMeta.field = field;
        return this;
    }

    /**
     * 设置注释
     *
     * @param comment comment
     * @return this
     */
    public DictTemplate comment(String comment) {
        dictMeta.comment = comment;
        return this;
    }

    /**
     * 设置字段
     *
     * @param fields fields
     * @return this
     */
    public DictTemplate fields(String... fields) {
        dictMeta.fields.addAll(Lists.of(fields));
        return this;
    }

    /**
     * 设置 label
     *
     * @param labels labels
     * @return this
     */
    public DictTemplate labels(String... labels) {
        dictMeta.labels.addAll(Lists.of(labels));
        return this;
    }

    /**
     * 设置 value
     *
     * @param values values
     * @return this
     */
    public DictTemplate values(Object... values) {
        dictMeta.values.addAll(Lists.of(values));
        return this;
    }

    /**
     * 设置 value 使用 fields
     *
     * @return this
     */
    public DictTemplate valueUseFields() {
        dictMeta.values.addAll(dictMeta.fields);
        return this;
    }

    /**
     * 添加 status
     *
     * @param status status
     * @return this
     */
    public DictTemplate status(Object... status) {
        return this.extra(Const.STATUS, status);
    }

    /**
     * 添加 type
     *
     * @param type type
     * @return this
     */
    public DictTemplate type(Object... type) {
        return this.extra(Const.TYPE, type);
    }

    /**
     * 添加 color
     *
     * @param colors colors
     * @return this
     */
    public DictTemplate color(Object... colors) {
        return this.extra(Const.COLOR, colors);
    }

    /**
     * 添加额外值
     *
     * @param key    key
     * @param values values
     * @return this
     */
    public DictTemplate extra(String key, Object... values) {
        // 初始化额外值
        if (dictMeta.extraValues.isEmpty()) {
            for (int i = 0; i < dictMeta.fields.size(); i++) {
                dictMeta.extraValues.add(new LinkedHashMap<>());
            }
        }
        // 设置额外值
        for (int i = 0; i < dictMeta.extraValues.size(); i++) {
            Object value = this.safeGet(i, values);
            dictMeta.extraValues.get(i).put(key, value);
        }
        return this;
    }

    /**
     * 获取值
     *
     * @param index index
     * @param list  list
     * @return value
     */
    private Object safeGet(int index, Object... list) {
        if (list == null) {
            return null;
        }
        if (list.length > index) {
            return list[index];
        } else {
            return null;
        }
    }

}
