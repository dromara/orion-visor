package com.orion.visor.framework.security.core.crypto;

import lombok.Data;

/**
 * 加密配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:14
 */
@Data
public class CryptoConfig {

    /**
     * 是否为默认加密器
     */
    protected boolean primary;

    /**
     * 是否启用
     */
    protected boolean enabled;

}
