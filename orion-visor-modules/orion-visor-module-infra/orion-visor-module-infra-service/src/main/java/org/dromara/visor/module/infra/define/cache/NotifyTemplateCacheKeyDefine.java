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
package org.dromara.visor.module.infra.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateCacheDTO;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateDetailCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 通知模板缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
public interface NotifyTemplateCacheKeyDefine {

    CacheKeyDefine NOTIFY_TEMPLATE_LIST = new CacheKeyBuilder()
            .key("notify:template:list:{}")
            .desc("通知模板列表 ${bizType}")
            .type(NotifyTemplateCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine NOTIFY_TEMPLATE_DETAIL = new CacheKeyBuilder()
            .key("notify:template:{}")
            .desc("通知模板详情 ${bizType}")
            .type(NotifyTemplateDetailCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
