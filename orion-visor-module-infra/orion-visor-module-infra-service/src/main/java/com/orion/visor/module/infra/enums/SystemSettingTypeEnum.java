package com.orion.visor.module.infra.enums;

import com.orion.visor.framework.common.handler.data.GenericsDataDefinition;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.handler.data.strategy.GenericsDataStrategy;
import com.orion.visor.module.infra.handler.setting.strategy.SftpSystemSettingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统配置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/9/27 19:07
 */
@Getter
@AllArgsConstructor
public enum SystemSettingTypeEnum implements GenericsDataDefinition {

    /**
     * SFTP 配置
     */
    SFTP(SftpSystemSettingStrategy.class),

    ;

    SystemSettingTypeEnum(Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass) {
        this.type = this.name();
        this.strategyClass = strategyClass;
    }

    private final String type;

    private final Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass;

    public static SystemSettingTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (SystemSettingTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
