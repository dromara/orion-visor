/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.convert;

import org.dromara.visor.module.infra.entity.domain.HistoryValueDO;
import org.dromara.visor.module.infra.entity.dto.history.HistoryValueCreateDTO;
import org.dromara.visor.module.infra.entity.dto.history.HistoryValueDTO;
import org.dromara.visor.module.infra.entity.request.history.HistoryValueCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 历史归档 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
@Mapper
public interface HistoryValueProviderConvert {

    HistoryValueProviderConvert MAPPER = Mappers.getMapper(HistoryValueProviderConvert.class);

    HistoryValueDTO to(HistoryValueDO domain);

    HistoryValueCreateRequest toRequest(HistoryValueCreateDTO request);

    List<HistoryValueDTO> toList(List<HistoryValueDO> list);

}
