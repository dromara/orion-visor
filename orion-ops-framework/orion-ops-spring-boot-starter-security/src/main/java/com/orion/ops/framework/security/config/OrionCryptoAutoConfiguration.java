package com.orion.ops.framework.security.config;

import com.orion.ops.framework.common.utils.CryptoUtils;
import com.orion.ops.framework.security.core.crypto.ValueCrypto;
import com.orion.ops.framework.security.core.crypto.aes.AesCryptoProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
public class OrionCryptoAutoConfiguration {

    @Resource
    private CryptoConfig config;

    /**
     * @return aes 加密器
     */
    @Primary
    @Bean(initMethod = "init")
    @ConditionalOnProperty(value = "orion.crypto.aes.enabled", havingValue = "true")
    public ValueCrypto aes() {
        AesCryptoProcessor processor = new AesCryptoProcessor(config.getAes());
        processor.init();
        // 设置工具委托类 委托需要与 @Primary 相同, 否则会导致工具类和bean的结果不同
        CryptoUtils.delegate = processor;
        return processor;
    }

}
