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

import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.entity.request.host.HostExtraUpdateRequest;
import org.dromara.visor.module.asset.handler.host.extra.HostExtraItemEnum;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSpecExtraModel;
import org.dromara.visor.module.asset.service.HostExtraService;
import org.dromara.visor.module.infra.api.DataExtraApi;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraQueryDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraSetDTO;
import org.dromara.visor.module.infra.enums.DataExtraTypeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 主机拓展信息 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 12:11
 */
@Service
public class HostExtraServiceImpl implements HostExtraService {

    @Resource
    private DataExtraApi dataExtraApi;

    @Override
    public Map<String, Object> getHostExtraView(Long hostId, String item) {
        HostExtraItemEnum extraItem = Valid.valid(HostExtraItemEnum::of, item);
        Long userId = this.getExtraUserId(extraItem);
        // 查询配置项
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(userId)
                .relId(hostId)
                .item(item)
                .build();
        String extraValue = dataExtraApi.getExtraValue(query, DataExtraTypeEnum.HOST);
        // 检查初始化并转为视图
        return this.checkItemAndToView(extraItem, extraValue, userId, hostId);
    }

    @Override
    public <T extends GenericsDataModel> T getHostExtra(Long userId, Long hostId, HostExtraItemEnum item) {
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(userId)
                .relId(hostId)
                .item(item.name())
                .build();
        String extraValue = dataExtraApi.getExtraValue(query, DataExtraTypeEnum.HOST);
        return item.parse(extraValue);
    }

    @Override
    public Map<Long, HostSpecExtraModel> getHostSpecMap(List<Long> hostIdList) {
        // 查询条件
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(Const.SYSTEM_USER_ID)
                .item(HostExtraItemEnum.SPEC.name())
                .relIdList(hostIdList)
                .build();
        // 查询
        return dataExtraApi.getExtraItems(query, DataExtraTypeEnum.HOST)
                .stream()
                .filter(s -> Strings.isNotBlank(s.getValue()))
                .collect(Collectors.toMap(DataExtraDTO::getRelId,
                        s -> HostExtraItemEnum.SPEC.toView(s.getValue())));
    }

    @Override
    public Integer updateHostExtra(HostExtraUpdateRequest request) {
        HostExtraItemEnum item = Valid.valid(HostExtraItemEnum::of, request.getItem());
        Long hostId = request.getHostId();
        Long userId = this.getExtraUserId(item);
        // 查询原始配置
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(userId)
                .relId(hostId)
                .item(item.name())
                .build();
        DataExtraDTO beforeExtraItem = dataExtraApi.getExtraItem(query, DataExtraTypeEnum.HOST);
        if (beforeExtraItem == null) {
            // 初始化并查询
            this.checkInitItem(item, userId, hostId);
            beforeExtraItem = dataExtraApi.getExtraItem(query, DataExtraTypeEnum.HOST);
        }
        GenericsDataModel newExtra = item.parse(request.getExtra());
        GenericsDataModel beforeExtra = item.parse(beforeExtraItem.getValue());
        // 更新验证
        item.doValid(beforeExtra, newExtra);
        // 更新配置
        return dataExtraApi.updateExtraValue(beforeExtraItem.getId(), newExtra.serial());
    }

    @Override
    public void copyHostExtra(Long originId, Long newId) {
        // 查询原始配置
        DataExtraQueryDTO query = DataExtraQueryDTO.builder()
                .userId(Const.SYSTEM_USER_ID)
                .relId(originId)
                .build();
        List<DataExtraDTO> items = dataExtraApi.getExtraItems(query, DataExtraTypeEnum.HOST);
        if (items.isEmpty()) {
            return;
        }
        // 插入新配置
        List<DataExtraSetDTO> newItems = items.stream()
                .map(s -> DataExtraSetDTO.builder()
                        .userId(Const.SYSTEM_USER_ID)
                        .relId(newId)
                        .item(s.getItem())
                        .value(s.getValue())
                        .build())
                .collect(Collectors.toList());
        dataExtraApi.addExtraItems(newItems, DataExtraTypeEnum.HOST);
    }

    /**
     * 检查配置项并且转为视图 (不存在则初始化默认值)
     *
     * @param item       item
     * @param extraValue extraValue
     * @param userId     userId
     * @param hostId     hostId
     * @return viewMap
     */
    private Map<String, Object> checkItemAndToView(HostExtraItemEnum item, String extraValue, Long userId, Long hostId) {
        if (extraValue == null) {
            // 初始化默认数据
            extraValue = this.checkInitItem(item, userId, hostId);
        }
        return item.toView(extraValue).toMap();
    }

    /**
     * 检查配置项 不存在则初始化默认值
     *
     * @param item   item
     * @param userId userId
     * @param hostId hostId
     * @return defaultValue
     */
    private String checkInitItem(HostExtraItemEnum item, Long userId, Long hostId) {
        // 初始化默认数据
        String extraValue = item.getDefault().serial();
        // 插入默认值
        DataExtraSetDTO set = DataExtraSetDTO.builder()
                .userId(userId)
                .relId(hostId)
                .item(item.name())
                .value(extraValue)
                .build();
        dataExtraApi.addExtraItem(set, DataExtraTypeEnum.HOST);
        return extraValue;
    }

    /**
     * 获取额外配置 userId
     *
     * @param item item
     * @return userId
     */
    private Long getExtraUserId(HostExtraItemEnum item) {
        return item.isUserExtra() ? SecurityUtils.getLoginUserId() : Const.SYSTEM_USER_ID;
    }

}
