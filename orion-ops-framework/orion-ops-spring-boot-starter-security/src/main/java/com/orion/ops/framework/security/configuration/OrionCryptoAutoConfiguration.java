package com.orion.ops.framework.security.configuration;

import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import com.orion.ops.framework.common.crypto.ValueCrypto;
import com.orion.ops.framework.security.configuration.config.CryptoConfig;
import com.orion.ops.framework.security.core.crypto.PrimaryValueCrypto;
import com.orion.ops.framework.security.core.crypto.aes.AesCryptoProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * 项目加密解密配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 23:59
 */
@AutoConfiguration
@EnableConfigurationProperties(CryptoConfig.class)
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_SECURITY_CRYPTO)
public class OrionCryptoAutoConfiguration {

    @Resource
    private CryptoConfig config;

    /**
     * @return 默认加密器
     */
    @Bean(name = "valueCrypto")
    @Primary
    public ValueCrypto primaryValueCrypto() {
        return new PrimaryValueCrypto();
    }

    /**
     * @return aes 加密器
     */
    @Bean(initMethod = "init")
    @ConditionalOnProperty(value = "orion.crypto.aes.enabled", havingValue = "true")
    public ValueCrypto aesValueCrypto() {
        return new AesCryptoProcessor(config.getAes());
    }

}
