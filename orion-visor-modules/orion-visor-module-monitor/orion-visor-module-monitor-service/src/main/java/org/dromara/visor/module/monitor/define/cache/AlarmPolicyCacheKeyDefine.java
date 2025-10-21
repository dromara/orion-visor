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
package org.dromara.visor.module.monitor.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.module.monitor.entity.dto.AlarmPolicyCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 监控告警策略缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
public interface AlarmPolicyCacheKeyDefine {

    CacheKeyDefine ALARM_POLICY = new CacheKeyBuilder()
            .key("alarm:policy:list:{}")
            .desc("告警策略 ${type}")
            .type(AlarmPolicyCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine ALARM_RULE_SILENCE = new CacheKeyBuilder()
            .key("alarm:silence:{}:{}")
            .desc("告警规则沉默标志 ${agentKey} ${ruleId}")
            .noPrefix()
            .type(Long.class)
            .struct(RedisCacheStruct.STRING)
            .build();

}
