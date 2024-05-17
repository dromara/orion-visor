package com.orion.visor.module.asset.handler.host.extra.model;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机拓展信息 - ssh 模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 21:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostExtraSshModel", description = "主机拓展信息 - ssh 模型")
public class HostSshExtraModel implements GenericsDataModel {

    @Schema(description = "认证方式")
    private String authType;

    @Schema(description = "认证方式")
    private String username;

    @Schema(description = "主机密钥")
    private Long keyId;

    @Schema(description = "主机身份")
    private Long identityId;

}
