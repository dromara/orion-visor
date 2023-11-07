package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.CacheKeyBuilder;
import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;

import java.util.concurrent.TimeUnit;

/**
 * 数据分组关联缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelCacheKeyDefine {

    CacheKeyDefine DATA_GROUP_REL = new CacheKeyBuilder()
            .key("data:group-rel:{}")
            .desc("数据分组关联 ${groupId}")
            .type(DataGroupRelCacheDTO.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

}
