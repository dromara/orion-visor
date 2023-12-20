package com.orion.ops.framework.common.handler.data.strategy;

import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;

/**
 * 标准数据处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:09
 */
public interface GenericsDataStrategy<Config extends GenericsDataModel, View> {

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    Config getDefault();

    /**
     * 更新填充
     *
     * @param before 修改前配置
     * @param after  修改后配置
     */
    void updateFill(Config before, Config after);

    /**
     * 预校验参数
     *
     * @param config config
     */
    void preValidConfig(Config config);

    /**
     * 校验参数
     *
     * @param config config
     */
    void validConfig(Config config);

    /**
     * 转为视图配置
     *
     * @param config config
     * @return 视图配置
     */
    View toView(String config);

}
