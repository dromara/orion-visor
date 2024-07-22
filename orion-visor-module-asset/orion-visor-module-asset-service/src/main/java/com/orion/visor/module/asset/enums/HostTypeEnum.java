package com.orion.visor.module.asset.enums;

import com.orion.visor.framework.common.handler.data.GenericsDataDefinition;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.handler.data.strategy.GenericsDataStrategy;
import com.orion.visor.module.asset.handler.host.config.strategy.HostSshConfigStrategy;
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
public enum HostTypeEnum implements GenericsDataDefinition {

    /**
     * SSH
     */
    SSH(HostSshConfigStrategy.class),

    ;

    private final Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass;

    public static HostTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
