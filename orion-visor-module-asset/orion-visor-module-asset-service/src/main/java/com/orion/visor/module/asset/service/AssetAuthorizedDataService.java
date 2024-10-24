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
package com.orion.visor.module.asset.service;

import com.orion.visor.module.asset.entity.request.asset.AssetAuthorizedDataQueryRequest;
import com.orion.visor.module.asset.entity.vo.AuthorizedHostWrapperVO;
import com.orion.visor.module.asset.entity.vo.HostIdentityVO;
import com.orion.visor.module.asset.entity.vo.HostKeyVO;
import com.orion.visor.module.asset.enums.HostTypeEnum;
import com.orion.visor.module.infra.enums.DataPermissionTypeEnum;

import java.util.List;

/**
 * 资产模块 授权数据服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/30 18:33
 */
public interface AssetAuthorizedDataService {

    /**
     * 获取已被授权的数据 id
     *
     * @param request request
     * @param type    type
     * @return dataId
     */
    List<Long> getAuthorizedDataRelId(DataPermissionTypeEnum type, AssetAuthorizedDataQueryRequest request);

    /**
     * 获取用户已授权的主机id 查询角色
     *
     * @param userId userId
     * @return hostId
     */
    List<Long> getUserAuthorizedHostId(Long userId);

    /**
     * 获取用户已授权&配置已启用的主机id 查询角色
     *
     * @param userId userId
     * @param type   type
     * @return hostId
     */
    List<Long> getUserAuthorizedEnabledHostId(Long userId, HostTypeEnum type);

    /**
     * 查询用户已授权的主机
     *
     * @param userId userId
     * @param type   type
     * @return group
     */
    AuthorizedHostWrapperVO getUserAuthorizedHost(Long userId, String type);

    /**
     * 查询用户已授权的主机密钥
     *
     * @param userId userId
     * @return key
     */
    List<HostKeyVO> getUserAuthorizedHostKey(Long userId);

    /**
     * 查询用户已授权的主机身份
     *
     * @param userId userId
     * @return identity
     */
    List<HostIdentityVO> getUserAuthorizedHostIdentity(Long userId);

}
