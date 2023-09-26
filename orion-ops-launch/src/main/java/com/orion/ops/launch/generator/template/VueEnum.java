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
public class VueEnum {

    /**
     * 替换的枚举字段 数据库/小驼峰
     */
    protected String variable;

    /**
     * 枚举类名 如果为空使用 field.propertyName + Enum
     */
    protected String className;

    /**
     * 枚举注释 如果为空使用 field.comment || className
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

    public VueEnum(String variable) {
        this(variable, null);
    }

    public VueEnum(String variable, String className) {
        this.className = className;
        this.variable = variable;
        this.names = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public VueEnum(String variable, String className, String comment, List<String> names, List<String> fields, List<List<Object>> values) {
        this.variable = variable;
        this.className = className;
        this.comment = comment;
        this.names = names;
        this.fields = fields;
        this.values = values;
    }

}
