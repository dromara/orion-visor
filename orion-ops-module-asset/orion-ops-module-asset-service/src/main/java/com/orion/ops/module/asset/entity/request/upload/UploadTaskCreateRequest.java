package com.orion.ops.module.asset.entity.request.upload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 上传任务 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UploadTaskCreateRequest", description = "上传任务 创建请求对象")
public class UploadTaskCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 1024)
    @Schema(description = "远程路径")
    private String remotePath;

    @Size(max = 128)
    @Schema(description = "描述")
    private String description;

    @NotEmpty
    @Schema(description = "上传主机id")
    private List<Long> hostIdList;

    @Valid
    @NotEmpty
    @Schema(description = "上传文件")
    private List<UploadTaskFileRequest> files;
}
