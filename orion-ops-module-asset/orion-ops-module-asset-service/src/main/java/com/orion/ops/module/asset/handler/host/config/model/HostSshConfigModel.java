package com.orion.ops.module.asset.handler.host.config.model;

import com.orion.ops.framework.common.security.UpdatePasswordAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class HostSshConfigModel implements HostConfigModel, UpdatePasswordAction {

    @NotNull
    @Range(min = 1, max = 65535)
    @Schema(description = "ssh 端口")
    private Integer port;

    @Size(max = 128)
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Size(max = 12)
    private String authType;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "身份id")
    private Long identityId;

    @Schema(description = "秘钥id")
    private Long keyId;

    @NotNull
    @Range(min = 0, max = 100000)
    @Schema(description = "连接超时时间")
    private Integer connectTimeout;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "SSH输出编码")
    private String charset;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "文件名称编码")
    private String fileNameCharset;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "文件内容编码")
    private String fileContentCharset;

    @Schema(description = "是否使用新密码 仅参数")
    private Boolean useNewPassword;

    @Schema(description = "是否已设置密码 仅返回")
    private Boolean hasPassword;

}
