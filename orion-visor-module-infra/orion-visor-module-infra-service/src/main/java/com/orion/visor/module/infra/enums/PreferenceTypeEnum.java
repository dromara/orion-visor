package com.orion.visor.module.infra.enums;

import com.orion.visor.framework.common.handler.data.GenericsDataDefinition;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.handler.data.strategy.GenericsDataStrategy;
import com.orion.visor.module.infra.handler.preference.strategy.SystemPreferenceStrategy;
import com.orion.visor.module.infra.handler.preference.strategy.TerminalPreferenceStrategy;
import lombok.Getter;

/**
 * 偏好类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 11:31
 */
@Getter
public enum PreferenceTypeEnum implements GenericsDataDefinition {

    /**
     * 系统偏好
     */
    SYSTEM(SystemPreferenceStrategy.class),

    /**
     * 终端偏好
     */
    TERMINAL(TerminalPreferenceStrategy.class),

    ;

    PreferenceTypeEnum(Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass) {
        this.type = this.name();
        this.strategyClass = strategyClass;
    }

    private final String type;

    private final Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass;

    public static PreferenceTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (PreferenceTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
