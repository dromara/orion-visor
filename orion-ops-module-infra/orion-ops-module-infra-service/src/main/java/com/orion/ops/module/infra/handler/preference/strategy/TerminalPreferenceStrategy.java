package com.orion.ops.module.infra.handler.preference.strategy;

import com.alibaba.fastjson.JSONObject;
import com.orion.ops.module.infra.handler.preference.model.TerminalPreferenceModel;
import org.springframework.stereotype.Component;

/**
 * 终端偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/8 14:46
 */
@Component
public class TerminalPreferenceStrategy implements IPreferenceStrategy<TerminalPreferenceModel> {

    @Override
    public TerminalPreferenceModel getDefault() {
        return TerminalPreferenceModel.builder()
                .newConnectionType("group")
                .displaySetting(TerminalPreferenceModel.DisplaySettingModel.builder()
                        .fontFamily("_")
                        .fontSize(12)
                        .lineHeight(1.00)
                        .fontWeight("normal")
                        .fontWeightBold("bold")
                        .cursorStyle("bar")
                        .cursorBlink(true)
                        .build()
                        .toJson()
                )
                .backgroundSetting(new JSONObject())
                .theme(new JSONObject())
                .build();
    }

}
