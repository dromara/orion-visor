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
package org.dromara.visor.module.infra.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.infra.api.DataExtraApi;
import org.dromara.visor.module.infra.convert.DataExtraProviderConvert;
import org.dromara.visor.module.infra.dao.DataExtraDAO;
import org.dromara.visor.module.infra.entity.domain.DataExtraDO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraQueryDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraSetDTO;
import org.dromara.visor.module.infra.entity.request.data.DataExtraQueryRequest;
import org.dromara.visor.module.infra.entity.request.data.DataExtraSetRequest;
import org.dromara.visor.module.infra.enums.DataExtraTypeEnum;
import org.dromara.visor.module.infra.service.DataExtraService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 数据拓展信息 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Slf4j
@Service
public class DataExtraApiImpl implements DataExtraApi {

    @Resource
    private DataExtraService dataExtraService;

    @Resource
    private DataExtraDAO dataExtraDAO;

    @Override
    public Integer setExtraItem(DataExtraSetDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        // 更新
        DataExtraSetRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.setExtraItem(request);
    }

    @Override
    public Long addExtraItem(DataExtraSetDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        // 更新
        DataExtraSetRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.addExtraItem(request);
    }

    @Override
    public void addExtraItems(List<DataExtraSetDTO> rows, DataExtraTypeEnum type) {
        for (DataExtraSetDTO row : rows) {
            Valid.valid(row);
        }
        List<DataExtraSetRequest> extras = rows.stream()
                .map(DataExtraProviderConvert.MAPPER::to)
                .peek(s -> s.setType(type.name()))
                .collect(Collectors.toList());
        dataExtraService.addExtraItems(extras);
    }

    @Override
    public Integer updateExtraValue(Long id, String value) {
        return dataExtraService.updateExtraValue(id, value);
    }

    @Override
    public void batchUpdateExtraValue(Map<Long, String> map) {
        dataExtraService.batchUpdateExtraValue(map);
    }

    @Override
    public String getExtraValue(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.allNotNull(dto.getUserId(), dto.getRelId(), dto.getItem());
        // 查询
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItemValue(request);
    }

    @Override
    public Map<Long, String> getExtraItemValues(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.allNotNull(dto.getUserId(), dto.getRelIdList(), dto.getItem());
        // 查询
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItemValues(request);
    }

    @Override
    public String getExtraItemValueByCache(Long userId, DataExtraTypeEnum type, String item, Long relId) {
        Valid.allNotNull(userId, type, item, relId);
        return dataExtraService.getExtraItemValueByCache(userId, type.name(), item, relId);
    }

    @Override
    public Map<Long, String> getExtraItemValuesByCache(Long userId, DataExtraTypeEnum type, String item) {
        Valid.allNotNull(userId, type, item);
        return dataExtraService.getExtraItemValuesByCache(userId, type.name(), item);
    }

    @Override
    public Future<Map<Long, String>> getExtraItemValuesByCacheAsync(Long userId, DataExtraTypeEnum type, String item) {
        return CompletableFuture.completedFuture(this.getExtraItemValuesByCache(userId, type, item));
    }

    @Override
    public Future<List<Map<Long, String>>> getExtraItemsValuesByCacheAsync(Long userId, DataExtraTypeEnum type, List<String> items) {
        Valid.allNotNull(userId, type);
        Valid.notEmpty(items);
        return CompletableFuture.completedFuture(dataExtraService.getExtraItemsValuesByCache(userId, type.name(), items));
    }

    @Override
    public DataExtraDTO getExtraItem(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.allNotNull(dto.getUserId(), dto.getRelId(), dto.getItem());
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        DataExtraDO extraItem = dataExtraService.getExtraItem(request);
        return DataExtraProviderConvert.MAPPER.to(extraItem);
    }

    @Override
    public List<DataExtraDTO> getExtraItems(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItems(request)
                .stream()
                .map(DataExtraProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Integer deleteByRelId(DataExtraTypeEnum type, Long relId) {
        return dataExtraService.deleteByRelId(type.name(), relId);
    }

    @Override
    public Integer deleteByRelIdList(DataExtraTypeEnum type, List<Long> relIdList) {
        return dataExtraService.deleteByRelIdList(type.name(), relIdList);
    }

    @Override
    public int deleteHostKeyExtra(List<Long> keyIdList) {
        Valid.notEmpty(keyIdList);
        return dataExtraDAO.deleteHostKey(keyIdList);
    }

    @Override
    public int deleteHostIdentityExtra(List<Long> identityIdList) {
        Valid.notEmpty(identityIdList);
        return dataExtraDAO.deleteHostIdentity(identityIdList);
    }

}
