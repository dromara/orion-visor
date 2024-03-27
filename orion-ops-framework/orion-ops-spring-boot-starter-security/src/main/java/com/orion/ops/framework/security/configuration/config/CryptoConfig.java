package com.orion.ops.framework.security.configuration.config;

import com.orion.ops.framework.security.core.crypto.aes.AesCryptoConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加密配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:01
 */
@Data
@ConfigurationProperties("orion.crypto")
public class CryptoConfig {

    /**
     * aes 加密器配置
     */
    private AesCryptoConfig aes;

}
