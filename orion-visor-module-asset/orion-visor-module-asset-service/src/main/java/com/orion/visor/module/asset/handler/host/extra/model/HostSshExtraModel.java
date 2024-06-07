package com.orion.visor.module.asset.handler.host.extra.model;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
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
public class HostSshExtraModel implements GenericsDataModel {

    /**
     * 认证方式
     */
    private String authType;

    /**
     * 认证方式
     */
    private String username;

    /**
     * 主机密钥
     */
    private Long keyId;

    /**
     * 主机身份
     */
    private Long identityId;

}
