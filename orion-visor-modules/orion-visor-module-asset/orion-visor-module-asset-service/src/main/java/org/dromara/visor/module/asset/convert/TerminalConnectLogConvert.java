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

import org.dromara.visor.module.asset.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.asset.entity.dto.TerminalConnectDTO;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogQueryRequest;
import org.dromara.visor.module.asset.entity.vo.TerminalConnectLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 终端连接日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface TerminalConnectLogConvert {

    TerminalConnectLogConvert MAPPER = Mappers.getMapper(TerminalConnectLogConvert.class);

    TerminalConnectLogDO to(TerminalConnectLogCreateRequest request);

    TerminalConnectLogDO to(TerminalConnectLogQueryRequest request);

    TerminalConnectLogVO to(TerminalConnectLogDO domain);

    TerminalConnectLogCreateRequest to(TerminalConnectDTO dto);

    List<TerminalConnectLogVO> to(List<TerminalConnectLogDO> list);

}
