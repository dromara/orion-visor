package com.orion.visor.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 执行主机日志查看 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecHostLogTailDTO", description = "执行主机日志查看 缓存对象")
public class ExecHostLogTailDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "输出编码")
    private String charset;

}
