/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.security.configuration;

import org.dromara.visor.framework.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.common.crypto.ValueCrypto;
import org.dromara.visor.framework.security.configuration.config.AesCryptoConfig;
import org.dromara.visor.framework.security.core.crypto.PrimaryValueCrypto;
import org.dromara.visor.framework.security.core.crypto.processor.AesCryptoProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 项目加密解密配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 23:59
 */
@AutoConfiguration
@EnableConfigurationProperties({AesCryptoConfig.class})
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_SECURITY_CRYPTO)
public class OrionCryptoAutoConfiguration {

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
    public ValueCrypto aesValueCrypto(AesCryptoConfig config) {
        return new AesCryptoProcessor(config);
    }

}
