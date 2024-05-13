package com.orion.ops.module.infra.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.ops.module.infra.entity.dto.FileUploadTokenDTO;

import java.util.concurrent.TimeUnit;

/**
 * 文明上传缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/31 11:48
 */
public interface FileUploadCacheKeyDefine {

    CacheKeyDefine FILE_UPLOAD = new CacheKeyBuilder()
            .key("file:upload:{}")
            .desc("文件上传信息 ${token}")
            .type(FileUploadTokenDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(3, TimeUnit.MINUTES)
            .build();

}
