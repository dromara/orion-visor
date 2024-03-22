package com.orion.ops.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;

import java.util.concurrent.TimeUnit;

/**
 * 命令执行服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 16:31
 */
public interface ExecCacheKeyDefine {

    CacheKeyDefine EXEC_TAIL = new CacheKeyBuilder()
            .key("exec:tail:{}")
            .desc("命令执行日志查看 ${token}")
            .type(ExecLogTailDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(5, TimeUnit.MINUTES)
            .build();

}
