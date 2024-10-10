package com.orion.visor.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * sftp 获取文件内容缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/10 20:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SftpGetContentCacheDTO", description = "sftp 获取文件内容缓存对象")
public class SftpGetContentCacheDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "文件路径")
    private String path;

}
