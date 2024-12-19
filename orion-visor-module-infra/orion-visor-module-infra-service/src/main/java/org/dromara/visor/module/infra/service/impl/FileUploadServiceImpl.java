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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.id.UUIds;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.infra.define.cache.FileUploadCacheKeyDefine;
import org.dromara.visor.module.infra.entity.dto.FileUploadTokenDTO;
import org.dromara.visor.module.infra.service.FileUploadService;
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
