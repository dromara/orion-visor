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
 * 数据分组缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupCacheKeyDefine {

    CacheKeyDefine DATA_GROUP = new CacheKeyBuilder()
            .key("data:group:{}")
            .desc("数据分组 ${type}")
            .type(DataGroupCacheDTO.class)
            .timeout(1, TimeUnit.DAYS)
            .build();

}
