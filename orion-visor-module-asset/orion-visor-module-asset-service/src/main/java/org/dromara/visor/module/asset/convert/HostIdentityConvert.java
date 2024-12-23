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
package org.dromara.visor.module.asset.convert;

import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.dto.HostIdentityCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostIdentityVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机身份 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostIdentityConvert {

    HostIdentityConvert MAPPER = Mappers.getMapper(HostIdentityConvert.class);

    HostIdentityDO to(HostIdentityCreateRequest request);

    HostIdentityDO to(HostIdentityUpdateRequest request);

    HostIdentityDO to(HostIdentityQueryRequest request);

    HostIdentityVO to(HostIdentityDO domain);

    HostIdentityVO to(HostIdentityCacheDTO cache);

    HostIdentityCacheDTO toCache(HostIdentityDO domain);

    List<HostIdentityVO> to(List<HostIdentityDO> list);

}
