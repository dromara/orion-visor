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
package org.dromara.visor.module.monitor.convert;

import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyDO;
import org.dromara.visor.module.monitor.entity.dto.AlarmPolicyCacheDTO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 监控告警策略 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
@Mapper
public interface AlarmPolicyConvert {

    AlarmPolicyConvert MAPPER = Mappers.getMapper(AlarmPolicyConvert.class);

    AlarmPolicyDO to(AlarmPolicyCreateRequest request);

    AlarmPolicyDO to(AlarmPolicyUpdateRequest request);

    AlarmPolicyDO to(AlarmPolicyQueryRequest request);

    AlarmPolicyVO to(AlarmPolicyDO domain);

    List<AlarmPolicyVO> to(List<AlarmPolicyDO> list);

    AlarmPolicyVO to(AlarmPolicyCacheDTO cache);

    AlarmPolicyCacheDTO toCache(AlarmPolicyDO domain);

}
