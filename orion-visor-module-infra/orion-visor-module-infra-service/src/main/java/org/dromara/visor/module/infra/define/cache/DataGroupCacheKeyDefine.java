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
package org.dromara.visor.module.infra.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.module.infra.entity.dto.DataGroupCacheDTO;
import org.dromara.visor.module.infra.entity.dto.DataGroupRelCacheDTO;

import java.util.concurrent.TimeUnit;

/**
 * 数据分组缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupCacheKeyDefine {

    CacheKeyDefine DATA_GROUP_LIST = new CacheKeyBuilder()
            .key("data:group-list:{}:{}")
            .desc("数据分组列表结构 ${type} ${userId}")
            .type(DataGroupCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine DATA_GROUP_TREE = new CacheKeyBuilder()
            .key("data:group-tree:{}:{}")
            .desc("数据分组树结构 ${type} ${userId}")
            .type(DataGroupCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine DATA_GROUP_REL_GROUP = new CacheKeyBuilder()
            .key("data:group-rel:group:{}")
            .desc("数据分组数据关联-分组 ${groupId}")
            .type(Long.class)
            .struct(RedisCacheStruct.LIST)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine DATA_GROUP_REL_TYPE = new CacheKeyBuilder()
            .key("data:group-rel:type:{}:{}")
            .desc("数据分组数据关联-类型 ${type} ${userId}")
            .type(DataGroupRelCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

}
