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
     * 请求实体包名
     */
    private String requestPackage;

    // -------------------- 前端 --------------------

    /**
     * 是否生成 vue 代码
     */
    private boolean genVue;

    /**
     * views 父目录
     */
    private String viewsPath;

    /**
     * api 文件目录
     */
    private String apiPath;

    /**
     * 功能 用于文件名称生成
     */
    private String feature;

    /**
     * 生成的枚举文件
     */
    private List<Class<? extends Enum<?>>> enums;

    public GenTable(String tableName, String comment, String requestPackage) {
        this.tableName = tableName;
        this.comment = comment;
        this.requestPackage = requestPackage;
        this.enums = new ArrayList<>();
    }

    /**
     * 生成 vue 模板
     *
     * @param viewsPath viewsPath
     * @param apiPath   apiPath
     * @param feature   feature
     * @return this
     */
    public GenTable vue(String viewsPath, String apiPath, String feature) {
        this.genVue = true;
        this.viewsPath = viewsPath;
        this.apiPath = apiPath;
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
