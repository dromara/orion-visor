package com.orion.ops.module.asset.handler.host.extra.strategy;

import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.module.asset.enums.HostExtraSshAuthTypeEnum;
import com.orion.ops.module.asset.handler.host.extra.model.HostSshExtraModel;
import org.springframework.stereotype.Component;

/**
 * 主机拓展信息 - ssh 模型处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:17
 */
@Component
public class HostSshExtraStrategy implements MapDataStrategy<HostSshExtraModel> {

    @Override
    public HostSshExtraModel getDefault() {
        return HostSshExtraModel.builder()
                .authType(HostExtraSshAuthTypeEnum.DEFAULT.name())
                .build();
    }

    @Override
    public void updateFill(HostSshExtraModel beforeModel, HostSshExtraModel afterModel) {

    }

    @Override
    public void preValid(HostSshExtraModel model) {

    }

    @Override
    public void valid(HostSshExtraModel model) {

    }

}
