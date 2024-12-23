/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.handler.preference.strategy;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.framework.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.module.infra.handler.preference.model.SystemPreferenceModel;
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
