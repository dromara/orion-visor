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
package org.dromara.visor.module.terminal.convert;

import org.dromara.visor.module.terminal.entity.domain.PathBookmarkDO;
import org.dromara.visor.module.terminal.entity.dto.PathBookmarkCacheDTO;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkCreateRequest;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkUpdateRequest;
import org.dromara.visor.module.terminal.entity.vo.PathBookmarkVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 路径标签 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Mapper
public interface PathBookmarkConvert {

    PathBookmarkConvert MAPPER = Mappers.getMapper(PathBookmarkConvert.class);

    PathBookmarkDO to(PathBookmarkCreateRequest request);

    PathBookmarkDO to(PathBookmarkUpdateRequest request);

    PathBookmarkVO to(PathBookmarkDO domain);

    List<PathBookmarkVO> to(List<PathBookmarkDO> list);

    PathBookmarkVO to(PathBookmarkCacheDTO cache);

    PathBookmarkCacheDTO toCache(PathBookmarkDO domain);

}
