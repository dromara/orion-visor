/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import org.dromara.visor.module.infra.entity.domain.DictValueDO;
import org.dromara.visor.module.infra.entity.request.dict.DictValueCreateRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictValueQueryRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictValueUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.DictValueVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 字典配置值 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface DictValueConvert {

    DictValueConvert MAPPER = Mappers.getMapper(DictValueConvert.class);

    DictValueDO to(DictValueCreateRequest request);

    DictValueDO to(DictValueUpdateRequest request);

    DictValueDO to(DictValueQueryRequest request);

    DictValueVO to(DictValueDO domain);

    List<DictValueVO> to(List<DictValueDO> list);

}
