package com.orion.ops.module.asset.handler.host.terminal.entity.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端连接请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TerminalConnectRequest", description = "终端连接请求 实体对象")
public class TerminalConnectRequest {

    @JSONField(name = "h")
    @Schema(description = "主机id")
    private String hostId;

}
