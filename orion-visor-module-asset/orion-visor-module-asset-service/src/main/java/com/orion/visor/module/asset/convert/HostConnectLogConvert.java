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
package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.domain.HostConnectLogDO;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.visor.module.asset.entity.vo.HostConnectLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机连接日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface HostConnectLogConvert {

    HostConnectLogConvert MAPPER = Mappers.getMapper(HostConnectLogConvert.class);

    HostConnectLogDO to(HostConnectLogCreateRequest request);

    HostConnectLogDO to(HostConnectLogQueryRequest request);

    HostConnectLogVO to(HostConnectLogDO domain);

    HostConnectLogCreateRequest to(HostTerminalConnectDTO dto);

    List<HostConnectLogVO> to(List<HostConnectLogDO> list);

}
