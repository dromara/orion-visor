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
package org.dromara.visor.module.exec.convert;

import org.dromara.visor.module.exec.entity.domain.ExecTemplateDO;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateCreateRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateUpdateRequest;
import org.dromara.visor.module.exec.entity.vo.ExecTemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 执行模板 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Mapper
public interface ExecTemplateConvert {

    ExecTemplateConvert MAPPER = Mappers.getMapper(ExecTemplateConvert.class);

    ExecTemplateDO to(ExecTemplateCreateRequest request);

    ExecTemplateDO to(ExecTemplateUpdateRequest request);

    ExecTemplateVO to(ExecTemplateDO domain);

}
