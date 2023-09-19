package com.orion.ops.module.asset.handler.host.config.strategy;

import com.orion.ops.module.asset.handler.host.config.model.HostConfigModel;

import java.util.Map;

/**
 * 主机配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 14:27
 */
public interface HostConfigStrategy<Config extends HostConfigModel> {

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    Config getDefault();

    /**
     * 插入填充
     *
     * @param config config
     */
    void insertFill(Config config);

    /**
     * 更新填充
     *
     * @param before 修改前配置
     * @param after  修改后配置
     */
    void updateFill(Config before, Config after);

    /**
     * 转为视图配置
     *
     * @param config config
     * @return 视图配置
     */
    Map<String, Object> toView(String config);

}
