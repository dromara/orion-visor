package com.orion.ops.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量执行启动对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecStartDTO", description = "批量执行启动对象")
public class ExecStartDTO {

    @Schema(description = "hostId")
    private Long logId;

    @Schema(description = "主机")
    private List<ExecStartHostDTO> hosts;

}
