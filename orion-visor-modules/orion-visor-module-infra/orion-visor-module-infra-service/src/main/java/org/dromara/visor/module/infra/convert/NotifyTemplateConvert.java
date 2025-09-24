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

import org.dromara.visor.module.infra.entity.domain.NotifyTemplateDO;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateCacheDTO;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateDetailCacheDTO;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateCreateRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateQueryRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.NotifyTemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 通知模板 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
@Mapper
public interface NotifyTemplateConvert {

    NotifyTemplateConvert MAPPER = Mappers.getMapper(NotifyTemplateConvert.class);

    NotifyTemplateDO to(NotifyTemplateCreateRequest request);

    NotifyTemplateDO to(NotifyTemplateUpdateRequest request);

    NotifyTemplateDO to(NotifyTemplateQueryRequest request);

    NotifyTemplateVO to(NotifyTemplateDO domain);

    List<NotifyTemplateVO> to(List<NotifyTemplateDO> list);

    NotifyTemplateVO to(NotifyTemplateCacheDTO cache);

    NotifyTemplateCacheDTO toCache(NotifyTemplateDO domain);

    NotifyTemplateDetailCacheDTO toDetailCache(NotifyTemplateDO domain);

}
