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

import com.orion.visor.module.infra.entity.domain.SystemMessageDO;
import com.orion.visor.module.infra.entity.request.message.SystemMessageCreateRequest;
import com.orion.visor.module.infra.entity.request.message.SystemMessageQueryRequest;
import com.orion.visor.module.infra.entity.vo.SystemMessageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 系统消息 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Mapper
public interface SystemMessageConvert {

    SystemMessageConvert MAPPER = Mappers.getMapper(SystemMessageConvert.class);

    SystemMessageDO to(SystemMessageCreateRequest request);

    SystemMessageDO to(SystemMessageQueryRequest request);

    SystemMessageVO to(SystemMessageDO domain);

    List<SystemMessageVO> to(List<SystemMessageDO> list);

}
