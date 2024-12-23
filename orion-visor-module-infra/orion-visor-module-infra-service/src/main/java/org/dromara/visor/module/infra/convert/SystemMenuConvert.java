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
package org.dromara.visor.module.infra.convert;

import org.dromara.visor.module.infra.entity.domain.SystemMenuDO;
import org.dromara.visor.module.infra.entity.dto.SystemMenuCacheDTO;
import org.dromara.visor.module.infra.entity.request.menu.*;
import org.dromara.visor.module.infra.entity.vo.SystemMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 菜单 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Mapper
public interface SystemMenuConvert {

    SystemMenuConvert MAPPER = Mappers.getMapper(SystemMenuConvert.class);

    SystemMenuValidMenuRequest toMenuValidate(SystemMenuDO domain);

    SystemMenuValidFunctionRequest toFunctionValidate(SystemMenuDO domain);

    SystemMenuDO to(SystemMenuCreateRequest request);

    SystemMenuDO to(SystemMenuUpdateRequest request);

    SystemMenuDO to(SystemMenuQueryRequest request);

    SystemMenuVO to(SystemMenuDO domain);

    SystemMenuVO to(SystemMenuCacheDTO cache);

    List<SystemMenuVO> to(List<SystemMenuDO> list);

    SystemMenuCacheDTO toCache(SystemMenuDO domain);

    List<SystemMenuCacheDTO> toCache(List<SystemMenuDO> list);

}
