package com.orion.ops.module.infra.handler.preference.strategy;

import com.orion.ops.module.infra.handler.preference.model.AppPreferenceModel;
import org.springframework.stereotype.Component;

/**
 * 系统偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:48
 */
@Component
public class SystemPreferenceStrategy implements IPreferenceStrategy<AppPreferenceModel> {

    @Override
    public AppPreferenceModel getDefault() {
        return AppPreferenceModel.builder()
                .menu(true)
                .topMenu(false)
                .navbar(true)
                .footer(true)
                .tabBar(true)
                .menuWidth(220)
                .colorWeak(false)
                .build();
    }

}
