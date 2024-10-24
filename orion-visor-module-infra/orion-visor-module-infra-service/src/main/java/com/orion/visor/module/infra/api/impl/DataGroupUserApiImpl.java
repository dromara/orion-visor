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

import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.DataGroupUserApi;
import com.orion.visor.module.infra.convert.DataGroupProviderConvert;
import com.orion.visor.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.visor.module.infra.enums.DataGroupTypeEnum;
import com.orion.visor.module.infra.service.DataGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据分组 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupUserApiImpl implements DataGroupUserApi {

    @Resource
    private DataGroupService dataGroupService;

    @Override
    public Long createDataGroup(DataGroupTypeEnum type, Long userId, DataGroupCreateDTO dto) {
        Valid.valid(dto);
        DataGroupCreateRequest request = DataGroupProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        request.setUserId(userId);
        return dataGroupService.createDataGroup(request);
    }

    @Override
    public List<DataGroupDTO> getDataGroupList(DataGroupTypeEnum type, Long userId) {
        List<DataGroupCacheDTO> rows = dataGroupService.getDataGroupListByCache(type.name(), userId);
        return DataGroupProviderConvert.MAPPER.toList(rows);
    }

    @Override
    public List<DataGroupDTO> getDataGroupTree(DataGroupTypeEnum type, Long userId) {
        List<DataGroupCacheDTO> rows = dataGroupService.getDataGroupTreeByCache(type.name(), userId);
        return DataGroupProviderConvert.MAPPER.toList(rows);
    }

    @Override
    public Integer deleteDataGroupByIdList(DataGroupTypeEnum type, Long userId, List<Long> idList) {
        return dataGroupService.deleteDataGroupByIdList(type.name(), userId, idList);
    }

}
