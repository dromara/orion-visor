package com.orion.ops.launch.generator.template;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举元数据
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 13:53
 */
@Data
public class EnumMeta {

    /**
     * 替换的枚举字段 数据库/小驼峰
     */
    protected String variable;

    /**
     * 枚举注释 没有的话使用 variable.comment || variable
     */
    protected String comment;

    /**
     * 枚举名称
     */
    protected List<String> names;

    /**
     * 枚举字段
     */
    protected List<String> fields;

    /**
     * 枚举值
     */
    protected List<List<Object>> values;

    public EnumMeta(String variable) {
        this.variable = variable;
        this.names = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public EnumMeta(String variable, String comment, List<String> names, List<String> fields, List<List<Object>> values) {
        this.variable = variable;
        this.comment = comment;
        this.names = names;
        this.fields = fields;
        this.values = values;
    }

    @Override
    public EnumMeta clone() {
        return new EnumMeta(variable, comment, names, fields, values);
    }

}
