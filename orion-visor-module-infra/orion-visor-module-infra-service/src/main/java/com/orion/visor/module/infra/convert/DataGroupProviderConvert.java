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
package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.DataGroupDO;
import com.orion.visor.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import com.orion.visor.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.visor.module.infra.entity.request.data.DataGroupMoveRequest;
import com.orion.visor.module.infra.entity.request.data.DataGroupRenameRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据分组 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupProviderConvert {

    DataGroupProviderConvert MAPPER = Mappers.getMapper(DataGroupProviderConvert.class);

    DataGroupDO to(DataGroupDTO dto);

    DataGroupDTO to(DataGroupDO domain);

    DataGroupDO to(DataGroupRenameDTO update);

    DataGroupCreateRequest toRequest(DataGroupCreateDTO request);

    DataGroupRenameRequest toRequest(DataGroupRenameDTO request);

    DataGroupMoveRequest toRequest(DataGroupMoveDTO request);

    List<DataGroupDTO> toList(List<DataGroupCacheDTO> list);

}
