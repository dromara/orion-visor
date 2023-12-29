package com.orion.ops.module.asset.handler.host.terminal.entity.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端连接响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TerminalConnectResponse", description = "终端连接响应 实体对象")
public class TerminalConnectResponse {

    @JSONField(name = "s")
    @Schema(description = "会话id")
    private String s;

}
