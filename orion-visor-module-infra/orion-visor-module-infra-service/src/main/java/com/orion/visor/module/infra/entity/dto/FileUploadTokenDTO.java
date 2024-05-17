package com.orion.visor.module.infra.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件上传 token 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileUploadTokenDTO", description = "文件上传 token 缓存对象")
public class FileUploadTokenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "上传父目录")
    private String endpoint;

}
