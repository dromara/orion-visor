package com.orion.ops.module.asset.handler.host.extra.strategy;

import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.module.asset.handler.host.extra.model.HostColorExtraModel;
import org.springframework.stereotype.Component;

/**
 * 主机拓展信息 - 颜色 模型处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/29 23:16
 */
@Component
public class HostColorExtraStrategy implements MapDataStrategy<HostColorExtraModel> {

    @Override
    public HostColorExtraModel getDefault() {
        return HostColorExtraModel.builder()
                // 默认透明
                .color("")
                .build();
    }

    @Override
    public void updateFill(HostColorExtraModel beforeModel, HostColorExtraModel afterModel) {
    }

    @Override
    public void preValid(HostColorExtraModel model) {
    }

    @Override
    public void valid(HostColorExtraModel model) {
    }

}
