package com.orion.visor.module.asset.entity.request.exec;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 批量执行日志 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecLogQueryRequest", description = "批量执行日志 查询请求对象")
public class ExecLogQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "执行用户id")
    private Long userId;

    @Schema(description = "执行用户名")
    private String username;

    @Size(max = 12)
    @Schema(description = "执行来源")
    private String source;

    @Schema(description = "执行来源id")
    private Long sourceId;

    @Size(max = 128)
    @Schema(description = "执行描述")
    private String description;

    @Schema(description = "执行命令")
    private String command;

    @Size(max = 12)
    @Schema(description = "执行状态")
    private String status;

    @Schema(description = "开始时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] startTimeRange;

}
