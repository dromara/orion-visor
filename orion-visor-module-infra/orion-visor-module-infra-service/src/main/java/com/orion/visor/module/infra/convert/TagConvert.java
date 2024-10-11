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

import com.orion.visor.module.infra.entity.domain.TagDO;
import com.orion.visor.module.infra.entity.dto.TagCacheDTO;
import com.orion.visor.module.infra.entity.request.tag.TagCreateRequest;
import com.orion.visor.module.infra.entity.request.tag.TagQueryRequest;
import com.orion.visor.module.infra.entity.vo.TagVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 标签枚举 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Mapper
public interface TagConvert {

    TagConvert MAPPER = Mappers.getMapper(TagConvert.class);

    TagDO to(TagCreateRequest request);

    TagDO to(TagQueryRequest request);

    TagVO to(TagDO domain);

    TagVO to(TagCacheDTO cache);

    TagCacheDTO toCache(TagDO domain);

    List<TagVO> to(List<TagDO> list);

}
