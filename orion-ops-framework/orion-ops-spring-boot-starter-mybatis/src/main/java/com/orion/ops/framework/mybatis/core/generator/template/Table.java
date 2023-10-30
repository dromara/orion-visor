package com.orion.ops.framework.mybatis.core.generator.template;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 10:44
 */
@Data
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
     * 是否生成导出
     */
    protected boolean enableExport;

    /**
     * 是否可缓存
     */
    protected boolean enableCache;

    /**
     * 是否生成操作日志
     */
    protected boolean enableOperatorLog;

    /**
     * 缓存的 key
     */
    protected String cacheKey;

    /**
     * 缓存描述
     */
    protected String cacheDesc;

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
     * 使用卡片视图
     */
    protected boolean enableCardView;

    /**
     * 生成的字典数据
     */
    protected List<DictMeta> dictList;

    protected Table() {
        this.dictList = new ArrayList<>();
    }

}
