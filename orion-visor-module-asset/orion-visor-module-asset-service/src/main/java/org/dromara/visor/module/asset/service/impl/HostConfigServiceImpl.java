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

import cn.orionsec.kit.lang.function.Functions;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.enums.HostStatusEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import org.dromara.visor.module.asset.service.HostConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public <T extends GenericsDataModel> T getHostConfig(Long id, HostTypeEnum type) {
        // 查询主机
        HostDO host = hostDAO.selectById(id);
        // 转换为配置
        return this.buildHostConfig(host, type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GenericsDataModel> T buildHostConfig(HostDO host, HostTypeEnum type) {
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 检查主机类型
        Valid.isTrue(type.name().equals(host.getType()), ErrorMessage.HOST_TYPE_ERROR);
        // 检查主机状态
        Valid.isTrue(HostStatusEnum.ENABLED.name().equals(host.getStatus()), ErrorMessage.HOST_NOT_ENABLED);
        // 查询主机配置
        HostSshConfigModel model = type.parse(host.getConfig());
        Valid.notNull(model, ErrorMessage.CONFIG_ABSENT);
        return (T) model;
    }

    @Override
    public <T extends GenericsDataModel> Map<Long, T> getHostConfigMap(List<Long> idList, HostTypeEnum type) {
        // 查询主机
        Map<Long, HostDO> hostMap = hostDAO.selectBatchIds(idList)
                .stream()
                .collect(Collectors.toMap(HostDO::getId, Function.identity(), Functions.right()));
        // 转换为配置
        Map<Long, T> result = new HashMap<>();
        for (Long id : idList) {
            result.put(id, this.buildHostConfig(hostMap.get(id), type));
        }
        return result;
    }

    @Override
    public <T extends GenericsDataModel> Map<Long, T> buildHostConfigMap(List<HostDO> hostList, HostTypeEnum type) {
        Map<Long, T> result = new HashMap<>();
        for (HostDO host : hostList) {
            result.put(host.getId(), this.buildHostConfig(host, type));
        }
        return result;
    }

}
