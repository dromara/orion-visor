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
package org.dromara.visor.module.asset.service;

import org.dromara.visor.module.asset.entity.request.asset.AssetDataGrantRequest;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;

/**
 * 资产模块 数据授权服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/30 18:33
 */
public interface AssetDataGrantService {

    /**
     * 主机分组授权
     *
     * @param request request
     */
    void grantHostGroup(AssetDataGrantRequest request);

    /**
     * 主机密钥授权
     *
     * @param request request
     */
    void grantHostKey(AssetDataGrantRequest request);

    /**
     * 主机身份授权
     *
     * @param request request
     */
    void grantHostIdentity(AssetDataGrantRequest request);

    /**
     * 数据授权
     *
     * @param type    type
     * @param request request
     */
    void grantData(DataPermissionTypeEnum type, AssetDataGrantRequest request);

}
