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

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字典元数据
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 13:53
 */
@Data
public class DictMeta {

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

    /**
     * 字典配置名称
     */
    protected String keyName;

    /**
     * 替换的字段 数据库/小驼峰
     */
    protected String variable;

    /**
     * 字段名称 如果为空使用 Strings.firstUpper(field.propertyName)
     */
    protected String field;

    /**
     * key 字段名称
     */
    private String keyField;

    /**
     * 注释 如果为空使用 field.comment || field
     */
    protected String comment;

    /**
     * 字段
     */
    protected List<String> fields;

    /**
     * label
     */
    protected List<String> labels;

    /**
     * value
     */
    protected List<Object> values;

    /**
     * 额外参数
     */
    protected List<Map<String, Object>> extraValues;

    /**
     * 额外参数 schema
     */
    private String extraSchema;

    /**
     * 额外参数 json
     */
    private List<String> extraJson;

    public DictMeta() {
    }

    public DictMeta(String keyName, String variable) {
        this(keyName, variable, null);
    }

    public DictMeta(String keyName, String variable, String field) {
        this.keyName = keyName;
        this.variable = variable;
        this.field = field;
        this.fields = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.values = new ArrayList<>();
        this.extraValues = new ArrayList<>();
    }

}
