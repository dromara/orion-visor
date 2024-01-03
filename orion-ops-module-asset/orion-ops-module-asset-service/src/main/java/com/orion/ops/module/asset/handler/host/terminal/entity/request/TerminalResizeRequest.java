package com.orion.ops.module.asset.handler.host.terminal.entity.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改大小请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 16:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TerminalResizeRequest", description = "修改大小请求 实体对象")
public class TerminalResizeRequest {

    // 连接主机 {"t":"rs","s": "1001","b":{"c":100,"r":20}}

    @JSONField(name = "c")
    @Schema(description = "列数")
    private Integer cols;

    @JSONField(name = "r")
    @Schema(description = "行数")
    private Integer rows;

}
