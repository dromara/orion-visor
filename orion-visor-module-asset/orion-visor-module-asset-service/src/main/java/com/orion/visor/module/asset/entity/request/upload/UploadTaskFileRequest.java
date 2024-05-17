package com.orion.visor.module.asset.entity.request.upload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 上传任务文件 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 22:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UploadTaskFileRequest", description = "上传任务文件 创建请求对象")
public class UploadTaskFileRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "文件id")
    private String fileId;

    @NotBlank
    @Schema(description = "文件地址")
    private String filePath;

    @NotNull
    @Schema(description = "文件大小")
    private Long fileSize;

}
