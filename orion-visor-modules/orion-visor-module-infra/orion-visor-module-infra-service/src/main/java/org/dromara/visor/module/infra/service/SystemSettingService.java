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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.request.system.SystemSettingUpdateBatchRequest;
import org.dromara.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.AppInfoVO;
import org.dromara.visor.module.infra.entity.vo.SystemSettingAggregateVO;
import org.dromara.visor.module.infra.handler.setting.model.EncryptSystemSettingModel;

/**
 * 系统设置服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/16 0:18
 */
public interface SystemSettingService {

    /**
     * 获取应用信息
     *
     * @return info
     */
    AppInfoVO getAppInfo();

    /**
     * 获取系统聚合设置
     *
     * @return setting
     */
    SystemSettingAggregateVO getSystemAggregateSetting();

    /**
     * 生成密钥对
     *
     * @return keypair
     */
    EncryptSystemSettingModel generatorKeypair();

    /**
     * 通过类型查询系统设置
     *
     * @param type type
     * @param <T>  T
     * @return row
     */
    <T> T getSystemSettingByType(String type);

    /**
     * 更新部分系统设置
     *
     * @param request request
     */
    void updateSystemSetting(SystemSettingUpdateRequest request);

    /**
     * 更新系统设置
     *
     * @param request request
     */
    void updateSystemSettingBatch(SystemSettingUpdateBatchRequest request);

}
