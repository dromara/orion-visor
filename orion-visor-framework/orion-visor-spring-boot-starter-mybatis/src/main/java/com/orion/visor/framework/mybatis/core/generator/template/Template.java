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
     * 设置字典
     *
     * @param keyName  字典配置名称
     * @param variable 替换字段 数据库/小驼峰
     * @return dict
     */
    public DictTemplate dict(String keyName, String variable) {
        return new DictTemplate(table, keyName, variable);
    }

    /**
     * 设置字典
     *
     * @param keyName  字典配置名称
     * @param variable 替换字段 数据库/小驼峰
     * @param field    字段名称
     * @return dict
     */
    public DictTemplate dict(String keyName, String variable, String field) {
        return new DictTemplate(table, keyName, variable, field);
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
