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
package org.dromara.visor.module.asset.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.module.asset.entity.dto.HostCacheDTO;
import org.dromara.visor.module.asset.entity.dto.HostIdentityCacheDTO;
import org.dromara.visor.module.asset.entity.dto.HostKeyCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 主机服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 13:54
 */
public interface HostCacheKeyDefine {

    CacheKeyDefine HOST_INFO = new CacheKeyBuilder()
            .key("host:info:list:{}")
            .desc("主机列表 ${type}")
            .type(HostCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine HOST_KEY = new CacheKeyBuilder()
            .key("host:key:list")
            .desc("主机密钥列表")
            .type(HostKeyCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine HOST_IDENTITY = new CacheKeyBuilder()
            .key("host:identity:list")
            .desc("主机身份列表")
            .type(HostIdentityCacheDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine HOST_INSTALL_LOG = new CacheKeyBuilder()
            .key("host:inst-log:{}")
            .desc("最新的主机安装记录 ${hostId}")
            .noPrefix()
            .type(Long.class)
            .struct(RedisCacheStruct.STRING)
            .build();

}
