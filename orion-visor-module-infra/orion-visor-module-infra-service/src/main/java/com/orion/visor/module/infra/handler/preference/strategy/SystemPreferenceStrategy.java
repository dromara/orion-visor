package com.orion.visor.module.infra.handler.preference.strategy;

import com.orion.visor.module.infra.handler.preference.model.SystemPreferenceModel;
import org.springframework.stereotype.Component;

/**
 * 系统偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:48
 */
@Component("systemPreferenceStrategy")
public class SystemPreferenceStrategy implements IPreferenceStrategy<SystemPreferenceModel> {

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

}
