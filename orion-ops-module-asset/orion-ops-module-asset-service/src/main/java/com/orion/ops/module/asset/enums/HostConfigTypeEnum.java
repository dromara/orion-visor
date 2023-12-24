package com.orion.ops.module.asset.enums;

import com.orion.ops.framework.common.enums.EnableStatus;
import com.orion.ops.framework.common.handler.data.GenericsDataDefinition;
import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;
import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.ops.module.asset.handler.host.config.strategy.HostSshConfigStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主机配置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/11 14:37
 */
@Getter
@AllArgsConstructor
public enum HostConfigTypeEnum implements GenericsDataDefinition {

    /**
     * SSH 配置
     */
    SSH("ssh",
            HostSshConfigModel.class,
            HostSshConfigStrategy.class,
            EnableStatus.ENABLED.getValue()),

    ;

    private final String type;

    private final Class<? extends GenericsDataModel> model;

    private final Class<? extends MapDataStrategy<? extends GenericsDataModel>> strategy;

    private final Integer defaultStatus;

    public static HostConfigTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostConfigTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
