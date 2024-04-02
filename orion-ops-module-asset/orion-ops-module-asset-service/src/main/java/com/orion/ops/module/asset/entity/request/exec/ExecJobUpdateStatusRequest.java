package com.orion.ops.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 计划执行任务 更新状态请求对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecJobUpdateStatusRequest", description = "计划执行任务 更新状态请求对象")
public class ExecJobUpdateStatusRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "启用状态 0禁用 1启用")
    private Integer status;

}
