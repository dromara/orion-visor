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
package org.dromara.visor.module.asset.convert;

import org.dromara.visor.module.asset.entity.domain.UploadTaskDO;
import org.dromara.visor.module.asset.entity.request.upload.UploadTaskCreateRequest;
import org.dromara.visor.module.asset.entity.request.upload.UploadTaskQueryRequest;
import org.dromara.visor.module.asset.entity.vo.UploadTaskStatusVO;
import org.dromara.visor.module.asset.entity.vo.UploadTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 上传任务 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Mapper
public interface UploadTaskConvert {

    UploadTaskConvert MAPPER = Mappers.getMapper(UploadTaskConvert.class);

    UploadTaskDO to(UploadTaskCreateRequest request);

    UploadTaskDO to(UploadTaskQueryRequest request);

    UploadTaskVO to(UploadTaskDO domain);

    List<UploadTaskVO> toList(List<UploadTaskDO> list);

    UploadTaskStatusVO toStatus(UploadTaskDO domain);

}
