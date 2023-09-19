package com.orion.ops.module.asset.handler.host.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 主机 SSH 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/13 16:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostSftpConfig", description = "主机 SSH 配置")
public class HostSshConfigModel implements HostConfigModel {

    @NotNull
    @Schema(description = "ssh 端口")
    private Integer port;

    @NotNull
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "身份id")
    private Long identityId;

    @NotNull
    @Schema(description = "连接超时时间")
    private Integer connectTimeout;

    @NotBlank
    @Schema(description = "SSH输出编码")
    private String charset;

    @NotBlank
    @Schema(description = "文件名称编码")
    private String fileNameCharset;

    @NotBlank
    @Schema(description = "文件内容编码")
    private String fileContentCharset;

}
