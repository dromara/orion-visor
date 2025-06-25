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
package org.dromara.visor.module.asset.api;

import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.enums.HostTypeEnum;

import java.util.List;

/**
 * 资产模块 授权数据对外服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 16:13
 */
public interface AssetAuthorizedDataApi {

    /**
     * 获取用户已授权&配置已启用的主机id 查询角色
     *
     * @param userId userId
     * @param type   type
     * @return hostId
     */
    List<Long> getUserAuthorizedEnabledHostId(Long userId, HostTypeEnum type);

    /**
     * 查询用户已授权并且启用的主机
     *
     * @param userId userId
     * @param type   type
     * @return group
     */
    List<HostDTO> getUserAuthorizedHostList(Long userId, HostTypeEnum type);

}
