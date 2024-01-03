package com.orion.ops.module.asset.handler.host.terminal.entity.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机连接检查响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TerminalCheckResponse", description = "主机连接检查响应 实体对象")
public class TerminalCheckResponse {

    @JSONField(name = "s")
    @Schema(description = "会话id")
    private String session;

    @JSONField(name = "r")
    @Schema(description = "检查结果")
    private Integer result;

    @JSONField(name = "em")
    @Schema(description = "错误信息")
    private String errorMessage;

}
