package com.orion.ops.launch.generator;

import com.orion.lang.utils.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 10:44
 */
@Data
public class GenTable {

    // -------------------- 后端 --------------------

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 业务注释
     */
    private String comment;

    /**
     * 业务实体包名
     * <p>
     * request dto 包
     */
    private String bizPackage;

    /**
     * 是否生成对外 api
     */
    private boolean genProviderApi;

    // -------------------- 前端 --------------------

    /**
     * 是否生成 vue 代码
     */
    private boolean genVue;

    /**
     * 模块 用于文件名称生成
     */
    private String module;

    /**
     * 功能 用于文件名称生成
     */
    private String feature;

    /**
     * 生成的枚举文件
     */
    private List<Class<? extends Enum<?>>> enums;

    public GenTable(String tableName, String comment, String bizPackage) {
        this(tableName, comment, bizPackage, false);
    }

    public GenTable(String tableName, String comment, String bizPackage, boolean genProviderApi) {
        this.tableName = tableName;
        this.comment = comment;
        this.bizPackage = bizPackage;
        this.genProviderApi = genProviderApi;
        this.enums = new ArrayList<>();
    }

    /**
     * 是否生成 api
     *
     * @return this
     */
    public GenTable api() {
        this.genProviderApi = true;
        return this;
    }

    /**
     * 生成 vue 模板
     *
     * @param module  module
     * @param feature feature
     * @return this
     */
    public GenTable vue(String module, String feature) {
        this.genVue = true;
        this.module = module;
        this.feature = feature;
        return this;
    }

    /**
     * 生成 vue 枚举对象
     *
     * @param enums enums
     * @return enums
     */
    @SafeVarargs
    public final GenTable enums(Class<? extends Enum<?>>... enums) {
        this.enums.addAll(Lists.of(enums));
        return this;
    }

}
