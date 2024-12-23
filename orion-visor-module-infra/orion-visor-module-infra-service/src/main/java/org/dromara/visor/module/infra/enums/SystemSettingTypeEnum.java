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
package org.dromara.visor.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.visor.framework.common.handler.data.GenericsDataDefinition;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.framework.common.handler.data.strategy.GenericsDataStrategy;
import org.dromara.visor.module.infra.handler.setting.strategy.SftpSystemSettingStrategy;

/**
 * 系统配置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/9/27 19:07
 */
@Getter
@AllArgsConstructor
public enum SystemSettingTypeEnum implements GenericsDataDefinition {

    /**
     * SFTP 配置
     */
    SFTP(SftpSystemSettingStrategy.class),

    ;

    SystemSettingTypeEnum(Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass) {
        this.type = this.name();
        this.strategyClass = strategyClass;
    }

    private final String type;

    private final Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> strategyClass;

    public static SystemSettingTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (SystemSettingTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
