package com.orion.ops.module.infra.handler.preference.strategy;

import com.orion.ops.module.infra.handler.preference.model.PreferenceModel;

/**
 * 偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:49
 */
public interface IPreferenceStrategy<Model extends PreferenceModel> {

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    Model getDefault();

}
