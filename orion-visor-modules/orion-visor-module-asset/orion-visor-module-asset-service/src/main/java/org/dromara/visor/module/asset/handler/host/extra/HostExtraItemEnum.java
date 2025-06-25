/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.handler.host.extra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.visor.common.handler.data.GenericsStrategyDefinition;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.common.handler.data.strategy.GenericsDataStrategy;
import org.dromara.visor.module.asset.handler.host.extra.strategy.HostLabelExtraStrategy;
import org.dromara.visor.module.asset.handler.host.extra.strategy.HostRdpExtraStrategy;
import org.dromara.visor.module.asset.handler.host.extra.strategy.HostSpecExtraStrategy;
import org.dromara.visor.module.asset.handler.host.extra.strategy.HostSshExtraStrategy;

/**
 * 主机额外配置项策略枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:48
 */
@Getter
@AllArgsConstructor
public enum HostExtraItemEnum implements GenericsStrategyDefinition {

    /**
     * 标签额外配置
     */
    LABEL(HostLabelExtraStrategy.class, true),

    /**
     * SSH 额外配置
     */
    SSH(HostSshExtraStrategy.class, true),

    /**
     * RDP 额外配置
     */
    RDP(HostRdpExtraStrategy.class, true),

    /**
     * 规格信息配置
     */
    SPEC(HostSpecExtraStrategy.class, false),

    ;

    private final Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass;

    private final boolean userExtra;

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
