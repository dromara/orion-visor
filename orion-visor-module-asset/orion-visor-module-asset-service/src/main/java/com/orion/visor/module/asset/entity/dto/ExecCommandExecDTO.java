package com.orion.visor.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量执行命令 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecCommandExecDTO", description = "批量执行命令对象")
public class ExecCommandExecDTO {

    @Schema(description = "执行用户id")
    private Long userId;

    @Schema(description = "执行用户名")
    private String username;

    @Schema(description = "执行来源")
    private String source;

    @Schema(description = "来源id")
    private Long sourceId;

    @Schema(description = "执行序列")
    private Integer execSeq;

    @Schema(description = "执行描述")
    private String description;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    private Integer scriptExec;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "参数 schema")
    private String parameterSchema;

    @Schema(description = "执行主机")
    private List<Long> hostIdList;

}
