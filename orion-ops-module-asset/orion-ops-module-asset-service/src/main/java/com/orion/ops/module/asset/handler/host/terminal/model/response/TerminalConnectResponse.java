package com.orion.ops.module.asset.handler.host.terminal.model.response;

import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 终端连接响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TerminalConnectResponse", description = "终端连接响应 实体对象")
public class TerminalConnectResponse extends TerminalBasePayload {

    @Schema(description = "检查结果")
    private Integer result;

    @Schema(description = "错误信息")
    private String msg;

}
