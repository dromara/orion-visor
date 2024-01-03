package com.orion.ops.module.asset.handler.host.terminal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 终端基础 payload
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 21:51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TerminalBasePayload", description = "终端基础 payload")
public class TerminalBasePayload {

    @Schema(description = "会话id")
    private String session;

    @Schema(description = "消息类型")
    private String type;

}
