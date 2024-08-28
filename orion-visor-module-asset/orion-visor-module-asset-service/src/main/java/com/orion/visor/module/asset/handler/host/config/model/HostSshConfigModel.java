package com.orion.visor.module.asset.handler.host.config.model;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.framework.common.security.UpdatePasswordAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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
public class HostSshConfigModel implements GenericsDataModel, UpdatePasswordAction {

    /**
     * 用户名
     */
    @Size(max = 128)
    private String username;

    /**
     * 认证方式
     */
    @NotBlank
    @Size(max = 12)
    private String authType;

    /**
     * 系统类型
     */
    @NotBlank
    @Size(max = 12)
    private String osType;

    /**
     * 密码
     */
    private String password;

    /**
     * 身份id
     */
    private Long identityId;

    /**
     * 密钥id
     */
    private Long keyId;

    /**
     * 连接超时时间
     */
    @NotNull
    @Min(value = 1)
    @Max(value = 100000)
    private Integer connectTimeout;

    /**
     * SSH输出编码
     */
    @NotBlank
    @Size(max = 12)
    private String charset;

    /**
     * 文件名称编码
     */
    @NotBlank
    @Size(max = 12)
    private String fileNameCharset;

    /**
     * 文件内容编码
     */
    @NotBlank
    @Size(max = 12)
    private String fileContentCharset;

    /**
     * 是否使用新密码 仅参数
     */
    private Boolean useNewPassword;

    /**
     * 是否已设置密码 仅返回
     */
    private Boolean hasPassword;

}
