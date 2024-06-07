package com.orion.visor.module.infra.handler.upload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 18:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {

    /**
     * type
     */
    private String type;

    /**
     * fileId
     */
    private String fileId;

}
