package com.orion.ops.module.infra.handler.upload.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "FileUploadRequest", description = "文件上传请求 实体对象")
public class FileUploadRequest {

    @Schema(description = "type")
    private String type;

    @Schema(description = "fileId")
    private String fileId;

}
