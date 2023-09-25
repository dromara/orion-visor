package com.orion.ops.launch.generator.template;

/**
 * 后端代码模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:14
 */
public class ServerTemplate extends Template {

    public ServerTemplate(Table table) {
        super(table);
        table.enableUnitTest = true;
    }

    public ServerTemplate(Table table, String tableName) {
        super(table);
        table.tableName = tableName;
        table.enableUnitTest = true;
    }

    public ServerTemplate(Table table, String tableName, String comment, String bizPackage) {
        super(table);
        table.tableName = tableName;
        table.comment = comment;
        table.bizPackage = bizPackage;
        table.enableUnitTest = true;
    }

    /**
     * 设置表名称
     *
     * @param tableName tableName
     * @return this
     */
    public ServerTemplate tableName(String tableName) {
        table.tableName = tableName;
        return this;
    }

    /**
     * 设置业务注释
     *
     * @param comment comment
     * @return this
     */
    public ServerTemplate comment(String comment) {
        table.comment = comment;
        return this;
    }

    /**
     * 设置业务实体包名
     *
     * @param bizPackage bizPackage
     * @return this
     */
    public ServerTemplate bizPackage(String bizPackage) {
        table.bizPackage = bizPackage;
        return this;
    }

    /**
     * 是否生成对外 api
     *
     * @return this
     */
    public ServerTemplate enableProviderApi() {
        table.enableProviderApi = true;
        return this;
    }

    /**
     * 不生成单元测试
     *
     * @return this
     */
    public ServerTemplate disableUnitTest() {
        table.enableUnitTest = false;
        return this;
    }

    /**
     * 设置 cache
     *
     * @return cache
     */
    public CacheTemplate cache() {
        return new CacheTemplate(table);
    }

}
