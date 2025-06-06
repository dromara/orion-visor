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
import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.enums.EnableStatus;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.asset.dao.HostConfigDAO;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.entity.domain.HostConfigDO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.request.host.HostConfigQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostConfigUpdateRequest;
import org.dromara.visor.module.asset.enums.HostStatusEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.service.HostConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Resource
    private HostConfigDAO hostConfigDAO;

    @Override
    public Integer updateHostConfig(HostConfigUpdateRequest request) {
        log.info("HostConfigService-updateHostConfig request: {}", request);
        Long hostId = request.getHostId();
        String type = request.getType();
        String param = OperatorLogs.toJsonString(JSON.parseObject(request.getConfig()));
        OperatorLogs.add(ExtraFieldConst.CONFIG, param);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        OperatorLogs.add(OperatorLogs.NAME, host.getName());
        // 获取处理策略
        HostTypeEnum strategy = HostTypeEnum.of(type);
        GenericsDataModel newConfig = strategy.parse(request.getConfig());
        // 查询配置
        HostConfigDO record = hostConfigDAO.selectByHostIdType(hostId, type);
        if (record == null) {
            // 新增验证
            strategy.doValid(null, newConfig);
            // 新增
            HostConfigDO entity = HostConfigDO.builder()
                    .hostId(hostId)
                    .type(type)
                    .status(EnableStatus.ENABLED.name())
                    .config(newConfig.serial())
                    .build();
            return hostConfigDAO.insert(entity);
        } else {
            // 修改验证
            GenericsDataModel beforeConfig = strategy.parse(record.getConfig());
            strategy.doValid(beforeConfig, newConfig);
            // 修改
            HostConfigDO entity = HostConfigDO.builder()
                    .id(record.getId())
                    .hostId(hostId)
                    .type(type)
                    .config(newConfig.serial())
                    .build();
            return hostConfigDAO.updateById(entity);
        }
    }

    @Override
    public void copyHostConfig(Long originId, Long newId, List<String> types) {
        // 查询原始主机配置
        Map<String, String> originHostConfigMap = hostConfigDAO.selectByHostId(originId)
                .stream()
                .collect(Collectors.toMap(HostConfigDO::getType,
                        HostConfigDO::getConfig,
                        Functions.right()));
        // 新增
        List<HostConfigDO> records = new ArrayList<>();
        for (String type : types) {
            // 获取原始配置
            String configValue = originHostConfigMap.get(type);
            if (Strings.isBlank(configValue)) {
                // 获取默认值
                configValue = HostTypeEnum.of(type).getDefault().serial();
            }
            HostConfigDO newConfig = HostConfigDO.builder()
                    .hostId(newId)
                    .type(type)
                    .status(EnableStatus.ENABLED.name())
                    .config(configValue)
                    .build();
            records.add(newConfig);
        }
        hostConfigDAO.insertBatch(records);
    }

    @Override
    public <T extends GenericsDataModel> T getHostConfig(Long hostId, String type) {
        // 查询配置信息
        HostConfigDO config = hostConfigDAO.selectByHostIdType(hostId, type);
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 检查配置状态
        Valid.isTrue(HostStatusEnum.ENABLED.name().equals(config.getStatus()), ErrorMessage.CONFIG_NOT_ENABLED);
        // 解析配置
        T model = HostTypeEnum.of(type).parse(config.getConfig());
        Valid.notNull(model, ErrorMessage.CONFIG_ABSENT);
        return model;
    }

    @Override
    public <T extends GenericsDataModel> T getHostConfigView(HostConfigQueryRequest request) {
        String type = request.getType();
        HostTypeEnum strategy = HostTypeEnum.of(type);
        // 查询配置
        HostConfigDO record = hostConfigDAO.selectByHostIdType(request.getHostId(), type);
        if (record == null) {
            // 获取默认值
            return strategy.toView(strategy.getDefault().serial());
        } else {
            return strategy.toView(record.getConfig());
        }
    }

}
