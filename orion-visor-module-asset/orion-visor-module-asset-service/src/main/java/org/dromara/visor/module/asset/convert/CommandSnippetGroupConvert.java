/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import org.dromara.visor.module.asset.entity.request.command.CommandSnippetGroupCreateRequest;
import org.dromara.visor.module.asset.entity.request.command.CommandSnippetGroupUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.CommandSnippetGroupVO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 命令片段分组 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Mapper
public interface CommandSnippetGroupConvert {

    CommandSnippetGroupConvert MAPPER = Mappers.getMapper(CommandSnippetGroupConvert.class);

    DataGroupCreateDTO to(CommandSnippetGroupCreateRequest request);

    DataGroupRenameDTO to(CommandSnippetGroupUpdateRequest request);

    CommandSnippetGroupVO to(DataGroupDTO domain);

}
