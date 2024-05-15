package com.orion.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 批量执行日志 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecLogVO", description = "批量执行日志 视图响应对象")
public class ExecLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "执行用户id")
    private Long userId;

    @Schema(description = "执行用户名")
    private String username;

    @Schema(description = "执行描述")
    private String description;

    @Schema(description = "执行序列")
    private Integer execSeq;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "参数 schema")
    private String parameterSchema;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    private Integer scriptExec;

    @Schema(description = "执行状态")
    private String status;

    @Schema(description = "执行开始时间")
    private Date startTime;

    @Schema(description = "执行完成时间")
    private Date finishTime;

    @Schema(description = "执行主机id")
    private List<Long> hostIdList;

    @Schema(description = "执行主机")
    private List<ExecHostLogVO> hosts;

}
