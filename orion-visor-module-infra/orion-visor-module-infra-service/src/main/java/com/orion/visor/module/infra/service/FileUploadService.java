package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.dto.FileUploadTokenDTO;

/**
 * 文件上传服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 15:58
 */
public interface FileUploadService {

    /**
     * 生成上传 uploadToken
     *
     * @param userId   userId
     * @param endpoint endpoint
     * @return token
     */
    String createUploadToken(Long userId, String endpoint);

    /**
     * 检查 uploadToken
     *
     * @param token token
     * @return info
     */
    FileUploadTokenDTO checkUploadToken(String token);

}
