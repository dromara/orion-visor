package com.orion.visor.module.asset.entity.request.upload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 上传任务 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UploadTaskRequest", description = "上传任务 请求对象")
public class UploadTaskRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "id")
    private Long id;

    @Schema(description = "是否失败")
    private Boolean failed;

}
