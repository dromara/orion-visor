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
package org.dromara.visor.framework.mybatis.core.generator.core;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.FieldConst;
import org.dromara.visor.framework.mybatis.core.generator.template.DictMeta;
import org.dromara.visor.framework.mybatis.core.generator.template.Table;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典解析器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/30 16:02
 */
public class DictParser {

    private final TableInfo tableInfo;

    private final Table table;

    public DictParser(TableInfo tableInfo, Table table) {
        this.tableInfo = tableInfo;
        this.table = table;
    }

    /**
     * 解析字典
     *
     * @return dictMap
     */
    public Map<String, DictMeta> parse() {
        // 字典值
        Map<String, DictMeta> dictMap = new LinkedHashMap<>();
        for (DictMeta meta : table.getDictList()) {
            // 检查字段是否存在
            String variable = meta.getVariable();
            TableField tableField = tableInfo.getFields()
                    .stream()
                    .filter(s -> variable.equals(s.getName()) || variable.equals(s.getPropertyName()))
                    .findFirst()
                    .orElseThrow(() -> Exceptions.runtime("未查询到字典映射字段 " + variable));
            // 设置字段名称
            if (meta.getField() == null) {
                meta.setField(Strings.firstUpper(tableField.getPropertyName()));
            }
            meta.setKeyField(meta.getField() + "Key");
            // 设置注释
            if (meta.getComment() == null) {
                meta.setComment(Strings.def(tableField.getComment(), meta.getField()));
            }
            // 设置额外参数 schema
            if (meta.getExtraValues().size() > 0) {
                List<Map<String, String>> extraSchema = meta.getExtraValues().get(0)
                        .keySet()
                        .stream()
                        .map(s -> {
                            Map<String, String> res = new LinkedHashMap<>();
                            res.put(FieldConst.NAME, s);
                            res.put(FieldConst.TYPE, "STRING");
                            return res;
                        }).collect(Collectors.toList());
                meta.setExtraSchema(JSON.toJSONString(extraSchema));
            } else {
                meta.setExtraSchema(Const.EMPTY_ARRAY);
            }
            // 设置额外参数 json
            List<String> extraJson = meta.getExtraValues()
                    .stream()
                    .map(JSON::toJSONString)
                    .collect(Collectors.toList());
            meta.setExtraJson(extraJson);

            dictMap.put(tableField.getPropertyName(), meta);
        }
        return dictMap;
    }

}
