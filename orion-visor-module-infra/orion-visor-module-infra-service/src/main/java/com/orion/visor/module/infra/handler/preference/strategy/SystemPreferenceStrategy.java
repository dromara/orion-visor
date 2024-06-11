package com.orion.visor.module.infra.handler.preference.strategy;

import com.orion.lang.utils.Exceptions;
import com.orion.visor.framework.common.handler.data.strategy.AbstractGenericsDataStrategy;
import com.orion.visor.module.infra.handler.preference.model.SystemPreferenceModel;
import org.springframework.stereotype.Component;

/**
 * 系统偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:48
 */
@Component
public class SystemPreferenceStrategy extends AbstractGenericsDataStrategy<SystemPreferenceModel> {

    public SystemPreferenceStrategy() {
        super(SystemPreferenceModel.class);
    }

    @Override
    public SystemPreferenceModel getDefault() {
        return SystemPreferenceModel.builder()
                .menu(true)
                .topMenu(false)
                .navbar(true)
                .footer(true)
                .tabBar(true)
                .menuWidth(220)
                .colorWeak(false)
                .defaultTablePageSize(10)
                .defaultCardPageSize(12)
                .build();
    }

    @Override
    public SystemPreferenceModel parse(String serialModel) {
        throw Exceptions.unsupported();
    }

}
