package com.orion.ops.module.asset.entity.request.exec;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 计划执行任务主机 查询请求对象
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
@Schema(name = "ExecJobHostQueryRequest", description = "计划执行任务主机 查询请求对象")
public class ExecJobHostQueryRequest extends PageRequest {

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "任务id")
    private Long jobId;

    @Schema(description = "主机id")
    private Long hostId;

}
