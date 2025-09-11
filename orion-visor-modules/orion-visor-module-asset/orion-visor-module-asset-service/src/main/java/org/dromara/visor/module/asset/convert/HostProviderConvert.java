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
package org.dromara.visor.module.asset.convert;

import org.dromara.visor.common.mapstruct.StringConversion;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.host.HostBaseDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostQueryDTO;
import org.dromara.visor.module.asset.entity.request.host.HostQueryRequest;
import org.dromara.visor.module.asset.entity.vo.HostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 主机 对外对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper(uses = StringConversion.class)
public interface HostProviderConvert {

    HostProviderConvert MAPPER = Mappers.getMapper(HostProviderConvert.class);

    HostQueryRequest to(HostQueryDTO dto);

    HostDO to(HostDTO host);

    HostDTO to(HostDO domain);

    HostDTO to(HostVO vo);

    HostBaseDTO toBase(HostDO domain);

}
