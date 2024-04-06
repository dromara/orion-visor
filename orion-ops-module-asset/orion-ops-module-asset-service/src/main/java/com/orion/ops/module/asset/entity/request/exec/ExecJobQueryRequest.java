package com.orion.ops.module.asset.entity.request.exec;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 计划执行任务 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecJobQueryRequest", description = "计划执行任务 查询请求对象")
public class ExecJobQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 64)
    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "启用状态 0禁用 1启用")
    private Integer status;

    @Schema(description = "是否查询最近执行任务")
    private Boolean queryRecentLog;

}
