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

import org.dromara.visor.module.asset.entity.request.path.PathBookmarkGroupCreateRequest;
import org.dromara.visor.module.asset.entity.request.path.PathBookmarkGroupUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.PathBookmarkGroupVO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 路径标签分组 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Mapper
public interface PathBookmarkGroupConvert {

    PathBookmarkGroupConvert MAPPER = Mappers.getMapper(PathBookmarkGroupConvert.class);

    DataGroupCreateDTO to(PathBookmarkGroupCreateRequest request);

    DataGroupRenameDTO to(PathBookmarkGroupUpdateRequest request);

    PathBookmarkGroupVO to(DataGroupDTO domain);

}
