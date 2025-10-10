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
package org.dromara.visor.module.monitor.convert;

import org.dromara.visor.common.mapstruct.StringConversion;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostQueryDTO;
import org.dromara.visor.module.monitor.entity.domain.MonitorHostDO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostContextDTO;
import org.dromara.visor.module.monitor.entity.request.host.MonitorHostQueryRequest;
import org.dromara.visor.module.monitor.entity.request.host.MonitorHostUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 监控主机 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Mapper(uses = StringConversion.class)
public interface MonitorHostConvert {

    MonitorHostConvert MAPPER = Mappers.getMapper(MonitorHostConvert.class);

    MonitorHostDO to(MonitorHostUpdateRequest request);

    MonitorHostVO to(MonitorHostDO domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hostId", source = "id")
    MonitorHostVO to(HostDTO dto);

    @Mapping(target = "config", ignore = true)
    MonitorHostContextDTO toContext(MonitorHostDO domain);

    HostQueryDTO toHostQuery(MonitorHostQueryRequest request);

}
