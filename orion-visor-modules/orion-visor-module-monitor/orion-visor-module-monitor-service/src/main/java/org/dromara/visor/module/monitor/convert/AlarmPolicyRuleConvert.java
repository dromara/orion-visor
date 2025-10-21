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

import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyRuleDO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyRuleVO;
import org.dromara.visor.module.monitor.handler.alarm.model.AlarmEngineRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 监控告警规则 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 13:39
 */
@Mapper
public interface AlarmPolicyRuleConvert {

    AlarmPolicyRuleConvert MAPPER = Mappers.getMapper(AlarmPolicyRuleConvert.class);

    AlarmPolicyRuleDO to(AlarmPolicyRuleCreateRequest request);

    AlarmPolicyRuleDO to(AlarmPolicyRuleUpdateRequest request);

    AlarmPolicyRuleVO to(AlarmPolicyRuleDO domain);

    @Mapping(target = "tags", ignore = true)
    AlarmEngineRule toEngine(AlarmPolicyRuleDO domain);

    List<AlarmPolicyRuleVO> to(List<AlarmPolicyRuleDO> list);

}
