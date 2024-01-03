package com.orion.ops.module.asset.handler.host.terminal.model.request;

import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 终端连接请求 实体对象
 * <p>
 * co|eff00a1|100|20
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
@Schema(name = "TerminalConnectRequest", description = "终端连接请求 实体对象")
public class TerminalConnectRequest extends TerminalBasePayload {

    @Schema(description = "列数")
    private Integer cols;

    @Schema(description = "行数")
    private Integer rows;

}
