package com.orion.ops.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
@Schema(name = "ExecCommandRequest", description = "批量执行命令 请求对象")
public class ExecCommandRequest {

    @Size(max = 128)
    @Schema(description = "执行描述")
    private String description;

    @NonNull
    @Schema(description = "超时时间")
    private Integer timeout;

    @NotBlank
    @Schema(description = "执行命令")
    private String command;

    @NotBlank
    @Schema(description = "参数 schema")
    private String parameterSchema;

    @NotEmpty
    @Schema(description = "执行主机")
    private List<Long> hostIdList;

}
