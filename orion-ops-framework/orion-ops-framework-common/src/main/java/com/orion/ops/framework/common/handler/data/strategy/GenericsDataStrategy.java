package com.orion.ops.framework.common.handler.data.strategy;

import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;

/**
 * 标准数据处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:09
 */
public interface GenericsDataStrategy<Model extends GenericsDataModel, View> {

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    Model getDefault();

    /**
     * 更新填充
     *
     * @param beforeModel 修改前的配置
     * @param afterModel  修改后的配置
     */
    void updateFill(Model beforeModel, Model afterModel);

    /**
     * 预校验参数
     *
     * @param model model
     */
    void preValid(Model model);

    /**
     * 校验参数
     *
     * @param model model
     */
    void valid(Model model);

    /**
     * 执行完整验证链
     * <p>
     * preValid > updateFill > preValid
     *
     * @param beforeModel beforeModel
     * @param afterModel  afterModel
     */
    default void doValidChain(Model beforeModel, Model afterModel) {
        // 预校验参数
        this.preValid(afterModel);
        // 更新填充
        this.updateFill(beforeModel, afterModel);
        // 校验参数
        this.valid(afterModel);
    }

    /**
     * 转为视图配置
     *
     * @param model model
     * @return 视图配置
     */
    View toView(String model);

}
