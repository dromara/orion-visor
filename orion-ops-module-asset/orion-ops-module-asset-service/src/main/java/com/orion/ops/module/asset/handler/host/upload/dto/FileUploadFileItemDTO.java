package com.orion.ops.module.asset.handler.host.upload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传文件对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 14:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileUploadFileItemDTO", description = "文件上传文件对象")
public class FileUploadFileItemDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "fileId")
    private String fileId;

    @Schema(description = "远程路径")
    private String remotePath;

    @Schema(description = "当前大小")
    private Long current;

    @Schema(description = "状态")
    private String status;

}
