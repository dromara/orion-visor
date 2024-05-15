package com.orion.visor.module.infra.entity.request.operator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 操作日志 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "OperatorLogQueryRequest", description = "操作日志 查询请求对象")
public class OperatorLogQueryRequest extends PageRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Size(max = 32)
    @Schema(description = "模块")
    private String module;

    @Size(max = 1)
    @Schema(description = "风险等级")
    private String riskLevel;

    @Size(max = 64)
    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "操作结果 0失败 1成功")
    private Integer result;

    @Schema(description = "开始时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] startTimeRange;

}
