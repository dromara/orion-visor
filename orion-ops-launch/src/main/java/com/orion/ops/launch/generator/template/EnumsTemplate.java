package com.orion.ops.launch.generator.template;

import com.orion.lang.utils.Enums;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.reflect.Fields;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端代码枚举模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:14
 */
public class EnumsTemplate extends VueTemplate {

    private final VueEnum vueEnum;

    public EnumsTemplate(Table table, String variable) {
        this(table, variable, (String) null);
    }

    public EnumsTemplate(Table table, String variable, String className) {
        super(table);
        this.vueEnum = new VueEnum(variable, className);
        table.enums.add(vueEnum);
    }

    public EnumsTemplate(Table table, String variable, Class<? extends Enum<?>> enumClass) {
        super(table);
        this.vueEnum = new VueEnum(variable);
        table.enums.add(vueEnum);
        this.parseEnumMeta(enumClass);
    }

    /**
     * 解析枚举
     *
     * @param enumClass enumClass
     */
    private void parseEnumMeta(Class<? extends Enum<?>> enumClass) {
        // 获取枚举
        List<? extends Enum<?>> enumList = Lists.of(enumClass.getEnumConstants());
        // 枚举名称
        List<String> names = enumList.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        // 枚举字段
        List<String> fields = Enums.getFields(enumClass);
        // 枚举值
        List<List<Object>> values = fields.stream()
                .map(field -> enumList.stream()
                        .map(enumItem -> Fields.getFieldValue(enumItem, field))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        vueEnum.className = enumClass.getSimpleName();
        vueEnum.names.addAll(names);
        vueEnum.fields.addAll(fields);
        vueEnum.values.addAll(values);
    }

    /**
     * 设置类名
     *
     * @param className className
     * @return this
     */
    public EnumsTemplate className(String className) {
        vueEnum.className = className;
        return this;
    }

    /**
     * 设置注释
     *
     * @param comment comment
     * @return this
     */
    public EnumsTemplate comment(String comment) {
        vueEnum.comment = comment;
        return this;
    }

    /**
     * 设置枚举名称
     *
     * @param names names
     * @return this
     */
    public EnumsTemplate names(String... names) {
        vueEnum.names.addAll(Lists.of(names));
        return this;
    }

    /**
     * 设置字段值
     *
     * @param values values
     * @return this
     */
    public EnumsTemplate values(String field, Object... values) {
        vueEnum.fields.add(field);
        vueEnum.values.add(Lists.of(values));
        return this;
    }

    /**
     * 添加 label
     *
     * @param labels labels
     * @return this
     */
    public EnumsTemplate label(Object... labels) {
        return this.values("label", labels);
    }

    /**
     * 添加 value
     *
     * @param values values
     * @return this
     */
    public EnumsTemplate value(Object... values) {
        return this.values("value", values);
    }

    /**
     * 添加 color
     *
     * @param colors colors
     * @return this
     */
    public EnumsTemplate color(Object... colors) {
        return this.values("color", colors);
    }

    /**
     * 添加 status
     *
     * @param status status
     * @return this
     */
    public EnumsTemplate status(Object... status) {
        return this.values("status", status);
    }

}
