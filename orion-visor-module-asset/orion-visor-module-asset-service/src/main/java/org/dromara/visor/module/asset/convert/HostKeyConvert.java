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
package org.dromara.visor.module.asset.convert;

import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.dto.HostKeyCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.HostKeyCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostKeyQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostKeyUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostKeyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机密钥 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostKeyConvert {

    HostKeyConvert MAPPER = Mappers.getMapper(HostKeyConvert.class);

    HostKeyDO to(HostKeyCreateRequest request);

    HostKeyDO to(HostKeyUpdateRequest request);

    HostKeyDO to(HostKeyQueryRequest request);

    HostKeyVO to(HostKeyDO domain);

    HostKeyVO to(HostKeyCacheDTO cache);

    HostKeyCacheDTO toCache(HostKeyDO domain);

    List<HostKeyVO> to(List<HostKeyDO> list);

}
