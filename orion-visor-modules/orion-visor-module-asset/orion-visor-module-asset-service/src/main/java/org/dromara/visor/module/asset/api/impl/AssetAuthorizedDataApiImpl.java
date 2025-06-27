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
package org.dromara.visor.module.asset.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.api.AssetAuthorizedDataApi;
import org.dromara.visor.module.asset.convert.HostProviderConvert;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.entity.vo.AuthorizedHostWrapperVO;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.service.AssetAuthorizedDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资产模块 授权数据对外服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 18:14
 */
@Slf4j
@Service
public class AssetAuthorizedDataApiImpl implements AssetAuthorizedDataApi {

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Override
    public List<Long> getUserAuthorizedEnabledHostId(Long userId, HostTypeEnum type) {
        return assetAuthorizedDataService.getUserAuthorizedEnabledHostId(userId, type.name());
    }

    @Override
    public List<HostDTO> getUserAuthorizedHostList(Long userId, HostTypeEnum type) {
        AuthorizedHostWrapperVO wrapper = assetAuthorizedDataService.getUserAuthorizedHost(userId, type.name());
        return wrapper.getHostList()
                .stream()
                .map(HostProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

}
