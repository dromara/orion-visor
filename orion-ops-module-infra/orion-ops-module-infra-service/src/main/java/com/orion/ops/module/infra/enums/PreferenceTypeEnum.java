package com.orion.ops.module.infra.enums;

import com.orion.ops.module.infra.handler.preference.model.PreferenceModel;
import com.orion.ops.module.infra.handler.preference.strategy.IPreferenceStrategy;
import com.orion.spring.SpringHolder;
import lombok.Getter;

/**
 * 偏好类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 11:31
 */
@Getter
public enum PreferenceTypeEnum {

    /**
     * 系统偏好
     */
    SYSTEM("systemPreferenceStrategy"),

    /**
     * 提示偏好
     */
    TIPS("tipsPreferenceStrategy"),

    ;

    PreferenceTypeEnum(String beanName) {
        this.type = this.name();
        this.beanName = beanName;
    }

    private final String type;

    /**
     * 策越 bean 名称
     * 可能跨模块所以不用 class
     */
    private final String beanName;

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

    /**
     * 获取策略
     *
     * @param <M> model
     * @param <T> type
     * @return IPreferenceStrategy
     */
    public <M extends PreferenceModel, T extends IPreferenceStrategy<M>> T getStrategy() {
        return SpringHolder.getBean(beanName);
    }

}
