package com.orion.visor.module.infra.handler.upload.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 18:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileUploadResponse", description = "文件上传响应 实体对象")
public class FileUploadResponse {

    @Schema(description = "type")
    private String type;

    @Schema(description = "fileId")
    private String fileId;

    @Schema(description = "路径")
    private String path;

}
