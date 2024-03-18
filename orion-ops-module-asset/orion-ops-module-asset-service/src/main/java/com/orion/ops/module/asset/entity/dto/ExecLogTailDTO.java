package com.orion.ops.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 执行日志查看 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecLogTailDTO", description = "执行日志查看 缓存对象")
public class ExecLogTailDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "token")
    private String token;

    @Schema(description = "执行主机")
    private List<ExecHostLogTailDTO> hosts;

}
