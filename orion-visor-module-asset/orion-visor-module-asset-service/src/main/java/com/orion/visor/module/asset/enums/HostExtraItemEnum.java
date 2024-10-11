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
package com.orion.visor.module.asset.enums;

import com.orion.visor.framework.common.handler.data.GenericsDataDefinition;
import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.handler.data.strategy.GenericsDataStrategy;
import com.orion.visor.module.asset.handler.host.extra.strategy.HostLabelExtraStrategy;
import com.orion.visor.module.asset.handler.host.extra.strategy.HostSshExtraStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主机额外配置项枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:48
 */
@Getter
@AllArgsConstructor
public enum HostExtraItemEnum implements GenericsDataDefinition {

    /**
     * SSH 额外配置
     */
    SSH(HostSshExtraStrategy.class),

    /**
     * 标签额外配置
     */
    LABEL(HostLabelExtraStrategy.class),

    ;

    private final Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass;

    public static HostExtraItemEnum of(String item) {
        if (item == null) {
            return null;
        }
        for (HostExtraItemEnum value : values()) {
            if (value.name().equals(item)) {
                return value;
            }
        }
        return null;
    }

}
