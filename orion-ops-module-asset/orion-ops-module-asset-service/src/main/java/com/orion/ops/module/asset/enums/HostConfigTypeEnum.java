package com.orion.ops.module.asset.enums;

import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.config.model.HostConfigModel;
import com.orion.ops.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.ops.module.asset.handler.host.config.strategy.HostConfigStrategy;
import com.orion.ops.module.asset.handler.host.config.strategy.HostSshConfigStrategy;
import com.orion.spring.SpringHolder;
import lombok.AllArgsConstructor;

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
    SSH(HostSshConfigModel.class, HostSshConfigStrategy.class, BooleanBit.TRUE.getValue()),

    ;

    private final Class<? extends HostConfigModel> type;

    private final Class<? extends HostConfigStrategy<? extends HostConfigModel>> strategy;

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

    public Class<? extends HostConfigModel> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <Config extends HostConfigModel, T extends HostConfigStrategy<Config>> T getStrategy() {
        return (T) SpringHolder.getBean(strategy);
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

}
