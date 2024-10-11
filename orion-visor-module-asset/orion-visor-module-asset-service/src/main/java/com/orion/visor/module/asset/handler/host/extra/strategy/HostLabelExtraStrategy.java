/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.asset.handler.host.extra.strategy;

import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.handler.data.strategy.AbstractGenericsDataStrategy;
import com.orion.visor.module.asset.handler.host.extra.model.HostLabelExtraModel;
import org.springframework.stereotype.Component;

/**
 * 主机拓展信息 - 标签模型处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/29 23:16
 */
@Component
public class HostLabelExtraStrategy extends AbstractGenericsDataStrategy<HostLabelExtraModel> {

    public HostLabelExtraStrategy() {
        super(HostLabelExtraModel.class);
    }

    @Override
    public HostLabelExtraModel getDefault() {
        return HostLabelExtraModel.builder()
                // 透明
                .color(Const.EMPTY)
                // 无别名
                .alias(Const.EMPTY)
                .build();
    }

    @Override
    public void updateFill(HostLabelExtraModel beforeModel, HostLabelExtraModel afterModel) {
        // 为空则覆盖
        if (afterModel.getAlias() == null) {
            afterModel.setAlias(beforeModel.getAlias());
        }
        if (afterModel.getColor() == null) {
            afterModel.setColor(beforeModel.getColor());
        }
    }

}
