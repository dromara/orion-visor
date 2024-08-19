package com.orion.visor.framework.mybatis.core.generator.core;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.FieldConst;
import com.orion.visor.framework.mybatis.core.generator.template.DictMeta;
import com.orion.visor.framework.mybatis.core.generator.template.Table;

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
