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

import org.dromara.visor.module.infra.entity.domain.DataExtraDO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraQueryDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataExtraSetDTO;
import org.dromara.visor.module.infra.entity.request.data.DataExtraQueryRequest;
import org.dromara.visor.module.infra.entity.request.data.DataExtraSetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据拓展信息 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Mapper
public interface DataExtraProviderConvert {

    DataExtraProviderConvert MAPPER = Mappers.getMapper(DataExtraProviderConvert.class);

    DataExtraQueryRequest to(DataExtraQueryDTO query);

    DataExtraSetRequest to(DataExtraSetDTO update);

    DataExtraDTO to(DataExtraDO domain);

}
