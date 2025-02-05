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
package org.dromara.visor.module.asset.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.enums.HostStatusEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.service.HostConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 主机配置 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Slf4j
@Service
public class HostConfigServiceImpl implements HostConfigService {

    @Resource
    private HostDAO hostDAO;

    @Override
    public <T extends GenericsDataModel> T getHostConfig(Long id) {
        // 查询主机
        HostDO host = hostDAO.selectById(id);
        // 转换为配置
        return this.getHostConfig(host);
    }

    @Override
    public <T extends GenericsDataModel> T getHostConfig(HostDO host) {
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        HostTypeEnum type = HostTypeEnum.of(host.getType());
        // 检查主机状态
        Valid.isTrue(HostStatusEnum.ENABLED.name().equals(host.getStatus()), ErrorMessage.HOST_NOT_ENABLED);
        // 查询主机配置
        T config = type.parse(host.getConfig());
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        return (T) config;
    }

}
