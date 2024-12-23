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
package org.dromara.visor.module.infra.handler.setting.strategy;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.framework.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.module.infra.handler.setting.model.SftpSystemSettingModel;
import org.springframework.stereotype.Component;

/**
 * SFTP 系统配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/9 11:44
 */
@Component
public class SftpSystemSettingStrategy extends AbstractGenericsDataStrategy<SftpSystemSettingModel> {

    public SftpSystemSettingStrategy() {
        super(SftpSystemSettingModel.class);
    }

    @Override
    public SftpSystemSettingModel getDefault() {
        return SftpSystemSettingModel.builder()
                .previewSize(2)
                .build();
    }

    @Override
    public SftpSystemSettingModel parse(String serialModel) {
        throw Exceptions.unsupported();
    }

}