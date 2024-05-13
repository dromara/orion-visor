package com.orion.ops.module.infra.api;

/**
 * 文件上传 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 15:58
 */
public interface FileUploadApi {

    /**
     * 生成上传 uploadToken
     *
     * @param userId   userId
     * @param endpoint endpoint
     * @return token
     */
    String createUploadToken(Long userId, String endpoint);

}
