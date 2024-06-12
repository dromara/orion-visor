package com.orion.visor.framework.common.handler.data.strategy;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;

/**
 * 标准数据处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:09
 */
public interface GenericsDataStrategy<M extends GenericsDataModel> {

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    M getDefault();

    /**
     * 执行完整验证链
     * <p>
     * preValid > updateFill > valid
     *
     * @param beforeModel beforeModel
     * @param afterModel  afterModel
     */
    void doValid(M beforeModel, M afterModel);

    /**
     * 解析数据
     *
     * @param serialModel serialModel
     * @return model
     */
    M parse(String serialModel);

    /**
     * 转为视图配置
     *
     * @param model model
     */
    void toView(M model);

}
