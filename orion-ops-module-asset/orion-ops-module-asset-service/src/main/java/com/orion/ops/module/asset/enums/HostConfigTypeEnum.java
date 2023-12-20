package com.orion.ops.module.asset.enums;

import com.orion.ops.framework.common.enums.EnableStatus;
import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;
import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.ops.module.asset.handler.host.config.strategy.HostSshConfigStrategy;
import com.orion.spring.SpringHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主机配置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/11 14:37
 */
@AllArgsConstructor
public enum HostConfigTypeEnum {

    /**
     * SSH 配置
     */
    SSH(HostSshConfigModel.class, HostSshConfigStrategy.class, EnableStatus.ENABLED.getValue()),

    ;

    @Getter
    private final Class<? extends GenericsDataModel> type;

    private final Class<? extends MapDataStrategy<? extends GenericsDataModel>> strategy;

    @Getter
    private final Integer defaultStatus;

    public static HostConfigTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostConfigTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <Config extends GenericsDataModel, T extends MapDataStrategy<Config>> T getStrategy() {
        return (T) SpringHolder.getBean(strategy);
    }

}
