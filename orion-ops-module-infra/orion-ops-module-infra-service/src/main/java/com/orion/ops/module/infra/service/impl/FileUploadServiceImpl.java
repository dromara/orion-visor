package com.orion.ops.module.infra.service.impl;

import com.orion.lang.id.UUIds;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.module.infra.define.cache.FileUploadCacheKeyDefine;
import com.orion.ops.module.infra.entity.dto.FileUploadTokenDTO;
import com.orion.ops.module.infra.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 文件上传服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 16:43
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Override
    public String createUploadToken(Long userId, String endpoint) {
        String token = UUIds.random32();
        String key = FileUploadCacheKeyDefine.FILE_UPLOAD.format(token);
        // 设置缓存
        FileUploadTokenDTO info = FileUploadTokenDTO.builder()
                .userId(userId)
                .endpoint(endpoint)
                .build();
        RedisStrings.setJson(key, FileUploadCacheKeyDefine.FILE_UPLOAD, info);
        return token;
    }

    @Override
    public FileUploadTokenDTO checkUploadToken(String token) {
        String key = FileUploadCacheKeyDefine.FILE_UPLOAD.format(token);
        // 查询缓存
        FileUploadTokenDTO info = RedisStrings.getJson(key, FileUploadCacheKeyDefine.FILE_UPLOAD);
        if (info != null) {
            // 删除缓存
            RedisStrings.delete(key);
        }
        return info;
    }

}
