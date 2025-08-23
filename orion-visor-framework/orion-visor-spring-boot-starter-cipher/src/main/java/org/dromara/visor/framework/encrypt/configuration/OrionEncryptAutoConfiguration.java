/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.framework.encrypt.configuration;

import org.dromara.visor.common.config.ConfigStore;
import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.common.cipher.AesEncryptor;
import org.dromara.visor.common.cipher.RsaDecryptor;
import org.dromara.visor.common.utils.AesEncryptUtils;
import org.dromara.visor.common.utils.RsaParamDecryptUtils;
import org.dromara.visor.framework.encrypt.configuration.config.AesEncryptConfig;
import org.dromara.visor.framework.encrypt.core.impl.AesEncryptorImpl;
import org.dromara.visor.framework.encrypt.core.impl.RsaDecryptorImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 项目加密解密配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/7 22:32
 */
@AutoConfiguration
@EnableConfigurationProperties({AesEncryptConfig.class})
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_ENCRYPT)
public class OrionEncryptAutoConfiguration {

    /**
     * @param config config
     * @return aes 加密器
     */
    @Bean
    public AesEncryptor aesEncryptor(AesEncryptConfig config) {
        // 加密器
        AesEncryptorImpl encryptor = new AesEncryptorImpl(config);
        // 设置工具类
        AesEncryptUtils.setDelegate(encryptor);
        return encryptor;
    }

    /**
     * @param configStore configStore
     * @return rsa 参数解密器
     */
    @Bean
    public RsaDecryptor rsaParamDecryptor(ConfigStore configStore) {
        // 解密器
        RsaDecryptor decryptor = new RsaDecryptorImpl(configStore);
        // 设置工具类
        RsaParamDecryptUtils.setDelegate(decryptor);
        return decryptor;
    }

}
