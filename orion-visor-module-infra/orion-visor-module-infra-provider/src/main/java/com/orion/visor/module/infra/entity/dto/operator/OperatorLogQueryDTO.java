package com.orion.visor.module.infra.entity.dto.operator;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 操作日志 查询对象
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
@Schema(name = "OperatorLogQueryDTO", description = "操作日志 查询对象")
public class OperatorLogQueryDTO extends PageRequest {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Size(max = 32)
    @Schema(description = "模块")
    private String module;

    @Size(max = 1)
    @Schema(description = "风险等级")
    private String riskLevel;

    @Size(max = 64)
    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "操作类型")
    private List<String> typeList;

    @Schema(description = "参数")
    private String extra;

    @Schema(description = "操作结果 0失败 1成功")
    private Integer result;

    @Schema(description = "开始时间区间 - 开始")
    private Date startTimeStart;

    @Schema(description = "开始时间区间 - 结束")
    private Date startTimeEnd;

}
