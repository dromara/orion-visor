package com.orion.visor.module.asset.enums;

import com.orion.visor.framework.common.handler.data.GenericsDataDefinition;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.visor.module.asset.handler.host.extra.model.HostLabelExtraModel;
import com.orion.visor.module.asset.handler.host.extra.model.HostSshExtraModel;
import com.orion.visor.module.asset.handler.host.extra.strategy.HostLabelExtraStrategy;
import com.orion.visor.module.asset.handler.host.extra.strategy.HostSshExtraStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主机额外配置项枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:48
 */
@Getter
@AllArgsConstructor
public enum HostExtraItemEnum implements GenericsDataDefinition {

    /**
     * SSH 额外配置
     */
    SSH("ssh", HostSshExtraModel.class, HostSshExtraStrategy.class),

    /**
     * 标签额外配置
     */
    LABEL("label", HostLabelExtraModel.class, HostLabelExtraStrategy.class),

    ;

    private final String item;

    private final Class<? extends GenericsDataModel> model;

    private final Class<? extends MapDataStrategy<? extends GenericsDataModel>> strategy;

    public static HostExtraItemEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostExtraItemEnum value : values()) {
            if (value.item.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
