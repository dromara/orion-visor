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
package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.domain.ExecJobDO;
import com.orion.visor.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.visor.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.visor.module.asset.entity.vo.ExecJobVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 计划任务 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobConvert {

    ExecJobConvert MAPPER = Mappers.getMapper(ExecJobConvert.class);

    ExecJobDO to(ExecJobCreateRequest request);

    ExecJobDO to(ExecJobUpdateRequest request);

    ExecJobDO to(ExecJobQueryRequest request);

    ExecJobVO to(ExecJobDO domain);

    List<ExecJobVO> to(List<ExecJobDO> list);

}
