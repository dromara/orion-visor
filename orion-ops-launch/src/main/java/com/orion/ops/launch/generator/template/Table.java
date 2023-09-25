package com.orion.ops.launch.generator.template;

import com.orion.lang.utils.collect.Lists;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 10:44
 */
@Data
@Getter
public class Table {

    // -------------------- 后端 --------------------

    /**
     * 表名称
     */
    protected String tableName;

    /**
     * 业务注释
     */
    protected String comment;

    /**
     * 业务实体包名
     * <p>
     * request dto 包
     */
    protected String bizPackage;

    /**
     * 是否生成对外 api
     */
    protected boolean enableProviderApi;

    /**
     * 是否生成单元测试
     */
    protected boolean enableUnitTest;

    /**
     * 是否可缓存
     */
    protected boolean enableCache;

    /**
     * 缓存的 key
     */
    protected String cacheKey;

    /**
     * 缓存是否会过期
     */
    protected boolean cacheExpired;

    /**
     * 缓存过期时间
     */
    protected Integer cacheExpireTime;

    /**
     * 缓存过期时间单位
     */
    protected TimeUnit cacheExpireUnit;

    // -------------------- 前端 --------------------

    /**
     * 是否生成 vue 代码
     */
    protected boolean enableVue;

    /**
     * 模块 用于文件名称生成
     */
    protected String module;

    /**
     * 功能 用于文件名称生成
     */
    protected String feature;

    /**
     * 使用抽屉表单
     */
    protected boolean enableDrawerForm;

    /**
     * 列表可多选
     */
    protected boolean enableRowSelection;

    /**
     * 生成的枚举文件
     * field name [k,v,k,v,k,v] label value color other
     */
    protected List<Class<? extends Enum<?>>> enums;

    public Table() {
    }

    public Table(String tableName, String comment, String bizPackage) {
        this.tableName = tableName;
        this.comment = comment;
        this.bizPackage = bizPackage;
        this.enableProviderApi = true;
        this.enableUnitTest = true;
        this.enums = new ArrayList<>();
    }

    /**
     * 忽略生成 api
     *
     * @return this
     */
    public Table ignoreApi() {
        this.enableProviderApi = false;
        return this;
    }

    /**
     * 忽略生成单元测试
     *
     * @return this
     */
    public Table ignoreTest() {
        this.enableUnitTest = false;
        return this;
    }

    /**
     * 生成 vue 模板
     *
     * @param module  module
     * @param feature feature
     * @return this
     */
    public Table vue(String module, String feature) {
        this.enableVue = true;
        this.module = module;
        this.feature = feature;
        return this;
    }

    /**
     * 使用抽屉表单
     *
     * @return this
     */
    public Table useDrawerForm() {
        return this;
    }

    /**
     * 生成 vue 枚举对象
     *
     * @param enums enums
     * @return enums
     */
    @SafeVarargs
    public final Table enums(Class<? extends Enum<?>>... enums) {
        this.enums.addAll(Lists.of(enums));
        return this;
    }

}
