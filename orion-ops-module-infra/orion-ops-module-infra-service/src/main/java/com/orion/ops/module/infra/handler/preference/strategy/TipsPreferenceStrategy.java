package com.orion.ops.module.infra.handler.preference.strategy;

import com.orion.ops.module.infra.handler.preference.model.TipsPreferenceModel;
import org.springframework.stereotype.Component;

/**
 * 提示偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 15:11
 */
@Component
public class TipsPreferenceStrategy implements IPreferenceStrategy<TipsPreferenceModel> {

    @Override
    public TipsPreferenceModel getDefault() {
        return TipsPreferenceModel.builder()
                .tippedSystemPreferenceModal(false)
                .build();
    }

}
