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
        // 默认显示设置
        JSONObject defaultDisplaySetting = TerminalPreferenceModel.DisplaySettingModel
                .builder()
                .fontFamily("_")
                .fontSize(14)
                .lineHeight(1.00)
                .fontWeight("normal")
                .fontWeightBold("bold")
                .cursorStyle("bar")
                .cursorBlink(true)
                .build()
                .toJson();
        return TerminalPreferenceModel.builder()
                .newConnectionType("group")
                .theme(new JSONObject())
                .displaySetting(defaultDisplaySetting)
                .actionBarSetting(new JSONObject())
                .backgroundSetting(new JSONObject())
                .build();
    }

}
