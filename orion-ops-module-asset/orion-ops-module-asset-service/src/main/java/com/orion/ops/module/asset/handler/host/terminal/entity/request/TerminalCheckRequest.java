package com.orion.ops.module.asset.handler.host.terminal.entity.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机连接检查请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TerminalConnectRequest", description = "主机连接检查请求 实体对象")
public class TerminalCheckRequest {

    // 连接主机 {"t":"ck","s": "1001","b":{"h":1}}

    @JSONField(name = "h")
    @Schema(description = "主机id")
    private Long hostId;

}
