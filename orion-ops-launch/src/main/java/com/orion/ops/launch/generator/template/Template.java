package com.orion.ops.launch.generator.template;

import lombok.Data;

/**
 * 代码生成模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 0:51
 */
@Data
public class Template {

    protected final Table table;

    protected Template(Table table) {
        this.table = table;
    }

    /**
     * 创建模板
     *
     * @param tableName 表名称
     * @return Template
     */
    public static ServerTemplate create(String tableName) {
        return new ServerTemplate(new Table(), tableName);
    }

    /**
     * 创建模板
     *
     * @param tableName  表名称
     * @param comment    业务注释
     * @param bizPackage 业务包名
     * @return Template
     */
    public static ServerTemplate create(String tableName, String comment, String bizPackage) {
        return new ServerTemplate(new Table(), tableName, comment, bizPackage);
    }

    /**
     * 设置 server
     *
     * @return ServerTemplate
     */
    public ServerTemplate server() {
        return new ServerTemplate(table);
    }

    /**
     * 设置 vue
     *
     * @return vue
     */
    public VueTemplate vue() {
        return new VueTemplate(table);
    }

    /**
     * 设置 vue
     *
     * @param module  模块
     * @param feature 功能
     * @return vue
     */
    public VueTemplate vue(String module, String feature) {
        return new VueTemplate(table, module, feature);
    }

    /**
     * 构建
     *
     * @return table
     */
    public Table build() {
        return table;
    }

}
