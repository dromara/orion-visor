package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 批量执行日志状态 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecLogStatusVO", description = "批量执行日志状态 视图响应对象")
public class ExecLogStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "执行状态列表")
    private List<ExecLogVO> logList;

    @Schema(description = "主机状态列表")
    private List<ExecHostLogVO> hostList;

}
