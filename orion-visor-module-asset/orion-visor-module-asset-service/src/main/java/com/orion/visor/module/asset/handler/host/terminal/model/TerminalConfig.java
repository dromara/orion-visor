package com.orion.visor.module.asset.handler.host.terminal.model;

import com.orion.visor.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机终端连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 23:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "TerminalConfig", description = "主机终端连接参数")
public class TerminalConfig {

    @Schema(description = "logId")
    private Long logId;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "主机名称")
    private String hostName;

    @Schema(description = "主机地址")
    private String address;

    @Schema(description = "cols")
    private Integer cols;

    @Schema(description = "rows")
    private Integer rows;

    @Schema(description = "SSH输出编码")
    private String charset;

    @Schema(description = "文件名称编码")
    private String fileNameCharset;

    @Schema(description = "文件内容编码")
    private String fileContentCharset;

}
