package com.orion.visor.framework.common.handler.data.strategy;

import com.alibaba.fastjson.JSON;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;

/**
 * 标准数据处理策略 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/11 21:44
 */
public abstract class AbstractGenericsDataStrategy<M extends GenericsDataModel> implements GenericsDataStrategy<M> {

    protected final Class<M> modelClass;

    public AbstractGenericsDataStrategy(Class<M> modelClass) {
        this.modelClass = modelClass;
    }

    /**
     * 更新填充
     *
     * @param beforeModel 修改前的配置
     * @param afterModel  修改后的配置
     */
    protected void updateFill(M beforeModel, M afterModel) {
    }

    /**
     * 预校验参数
     *
     * @param model model
     */
    protected void preValid(M model) {
    }

    /**
     * 校验参数
     *
     * @param model model
     */
    protected void valid(M model) {
    }

    @Override
    public void doValid(M beforeModel, M afterModel) {
        // 预校验参数
        this.preValid(afterModel);
        // 更新填充
        this.updateFill(beforeModel, afterModel);
        // 校验参数
        this.valid(afterModel);
    }

    @Override
    public M parse(String serialModel) {
        return JSON.parseObject(serialModel, modelClass);
    }

    @Override
    public void toView(M model) {
    }

}
