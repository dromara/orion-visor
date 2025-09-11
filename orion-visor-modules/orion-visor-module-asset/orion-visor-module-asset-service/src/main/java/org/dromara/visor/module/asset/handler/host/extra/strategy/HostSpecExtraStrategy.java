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
package org.dromara.visor.module.asset.handler.host.extra.strategy;

import org.dromara.visor.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSpecExtraModel;
import org.springframework.stereotype.Component;

/**
 * 主机规格额外信息策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/24 0:21
 */
@Component
public class HostSpecExtraStrategy extends AbstractGenericsDataStrategy<HostSpecExtraModel> {

    public HostSpecExtraStrategy() {
        super(HostSpecExtraModel.class);
    }

    @Override
    public HostSpecExtraModel getDefault() {
        return new HostSpecExtraModel();
    }

    @Override
    protected void updateFill(HostSpecExtraModel beforeModel, HostSpecExtraModel afterModel) {
        afterModel.setSynced(beforeModel.getSynced());
    }

}
