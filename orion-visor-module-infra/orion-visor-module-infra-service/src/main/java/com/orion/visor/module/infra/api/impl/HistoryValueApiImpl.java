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
package com.orion.visor.module.infra.api.impl;

import com.alibaba.fastjson.JSON;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.HistoryValueApi;
import com.orion.visor.module.infra.convert.HistoryValueProviderConvert;
import com.orion.visor.module.infra.entity.domain.HistoryValueDO;
import com.orion.visor.module.infra.entity.dto.history.HistoryValueCreateDTO;
import com.orion.visor.module.infra.entity.dto.history.HistoryValueDTO;
import com.orion.visor.module.infra.entity.request.history.HistoryValueCreateRequest;
import com.orion.visor.module.infra.enums.HistoryValueTypeEnum;
import com.orion.visor.module.infra.service.HistoryValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 历史归档 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
@Slf4j
@Service
public class HistoryValueApiImpl implements HistoryValueApi {

    @Resource
    private HistoryValueService historyValueService;

    @Override
    public Long createHistoryValue(HistoryValueTypeEnum type, HistoryValueCreateDTO dto) {
        log.info("HistoryValueApi.createHistoryValue dto: {}", JSON.toJSONString(dto));
        Valid.notNull(type);
        Valid.valid(dto);
        // 转换
        HistoryValueCreateRequest request = HistoryValueProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        // 创建
        return historyValueService.createHistoryValue(request);
    }

    @Override
    public HistoryValueDTO getHistoryValueById(Long id) {
        log.info("HistoryValueApi.getHistoryValueById id: {}", id);
        Valid.notNull(id, ErrorMessage.ID_MISSING);
        // 修改
        HistoryValueDO record = historyValueService.getHistoryById(id);
        if (record == null) {
            return null;
        }
        // 转换
        return HistoryValueProviderConvert.MAPPER.to(record);
    }

    @Override
    public HistoryValueDTO getHistoryValueByRelId(Long id, Long relId, HistoryValueTypeEnum type) {
        log.info("HistoryValueApi.getHistoryValueByRelId id: {}, relId: {}, type: {}", id, relId, type);
        Valid.allNotNull(id, relId, type);
        // 修改
        HistoryValueDO record = historyValueService.getHistoryByRelId(id, relId, type.name());
        if (record == null) {
            return null;
        }
        // 转换
        return HistoryValueProviderConvert.MAPPER.to(record);
    }

    @Override
    public Integer deleteByRelId(HistoryValueTypeEnum type, Long relId) {
        return historyValueService.deleteByRelId(type.name(), relId);
    }

    @Override
    public Integer deleteByRelIdList(HistoryValueTypeEnum type, List<Long> relIdList) {
        return historyValueService.deleteByRelIdList(type.name(), relIdList);
    }

}
