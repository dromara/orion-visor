package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.ops.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.ops.module.infra.entity.dto.DataGroupRelCacheDTO;

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
            .key("data:group-list:{}")
            .desc("数据分组列表结构 ${type}")
            .type(DataGroupCacheDTO.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

    CacheKeyDefine DATA_GROUP_TREE = new CacheKeyBuilder()
            .key("data:group-tree:{}")
            .desc("数据分组树结构 ${type}")
            .type(DataGroupCacheDTO.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

    CacheKeyDefine DATA_GROUP_REL_GROUP = new CacheKeyBuilder()
            .key("data:group-rel:group:{}")
            .desc("数据分组数据关联-分组 ${groupId}")
            .type(DataGroupRelCacheDTO.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

    CacheKeyDefine DATA_GROUP_REL_TYPE = new CacheKeyBuilder()
            .key("data:group-rel:type:{}")
            .desc("数据分组数据关联-类型 ${type}")
            .type(DataGroupRelCacheDTO.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

}
