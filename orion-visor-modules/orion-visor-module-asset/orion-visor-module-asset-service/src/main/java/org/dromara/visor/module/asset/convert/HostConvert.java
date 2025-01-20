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

import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.HostCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.HostCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostBaseVO;
import org.dromara.visor.module.asset.entity.vo.HostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostConvert {

    HostConvert MAPPER = Mappers.getMapper(HostConvert.class);

    HostDO to(HostCreateRequest request);

    HostDO to(HostUpdateRequest request);

    HostDO to(HostQueryRequest request);

    HostVO to(HostDO domain);

    HostVO to(HostCacheDTO cache);

    HostCacheDTO toCache(HostDO domain);

    HostBaseVO toBase(HostDO domain);

    List<HostVO> toList(List<HostDO> domain);

    List<HostBaseVO> toBaseList(List<HostDO> domain);

}
