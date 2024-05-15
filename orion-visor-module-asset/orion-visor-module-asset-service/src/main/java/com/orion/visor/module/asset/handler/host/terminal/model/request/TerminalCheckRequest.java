package com.orion.visor.module.asset.handler.host.terminal.model.request;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 主机连接检查请求 实体对象
 * <p>
 * ck|eff00a1|1
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
@Schema(name = "TerminalConnectRequest", description = "主机连接检查请求 实体对象")
public class TerminalCheckRequest extends TerminalBasePayload {

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "连接类型")
    private String connectType;

}
