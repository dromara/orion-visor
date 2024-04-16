package com.orion.ops.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 计划任务 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecJobCreateRequest", description = "计划任务 创建请求对象")
public class ExecJobCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "任务名称")
    private String name;

    @NotBlank
    @Size(max = 512)
    @Schema(description = "cron 表达式")
    private String expression;

    @NotNull
    @Schema(description = "超时时间")
    private Integer timeout;

    @NonNull
    @Schema(description = "是否使用脚本执行")
    private Integer scriptExec;

    @NotBlank
    @Schema(description = "执行命令")
    private String command;

    @NotBlank
    @Schema(description = "命令参数")
    private String parameterSchema;

    @NotEmpty
    @Schema(description = "执行主机")
    private List<Long> hostIdList;

}
