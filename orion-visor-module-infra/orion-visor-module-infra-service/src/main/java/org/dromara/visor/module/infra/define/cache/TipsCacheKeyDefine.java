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
package org.dromara.visor.module.infra.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * tips 服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 17:58
 */
public interface TipsCacheKeyDefine {

    CacheKeyDefine TIPS = new CacheKeyBuilder()
            .key("user:tips:{}")
            .desc("user:tips ${userId} 90天不会重复提示")
            .type(String.class)
            .struct(RedisCacheStruct.LIST)
            .timeout(90, TimeUnit.DAYS)
            .build();

}
