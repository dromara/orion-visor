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
package org.dromara.visor.framework.encrypt.configuration.config;

import cn.orionsec.kit.lang.utils.crypto.CryptoConst;
import cn.orionsec.kit.lang.utils.crypto.enums.PaddingMode;
import cn.orionsec.kit.lang.utils.crypto.enums.WorkingMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * aes 加密器配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 22:22
 */
@Data
@ConfigurationProperties("orion.encrypt.aes")
public class AesEncryptConfig {

    /**
     * 加密模式
     */
    private WorkingMode workingMode;

    /**
     * 填充模式
     */
    private PaddingMode paddingMode;

    /**
     * 加密密钥
     */
    private String secretKey;

    /**
     * 是否生成密钥
     */
    private boolean useGeneratorKey;

    /**
     * 生成的密钥长度 128 192 256bytes
     */
    private int generatorKeyLength;

    /**
     * 向量 长度为 16bytes
     * 除 ECB/GCM 外的工作模式
     */
    private String iv;

    /**
     * GCM 模式参数 长度为 96 104 112 120 128bytes
     */
    private String gcm;

    /**
     * GCM 模式 aad
     */
    private String aad;

    public AesEncryptConfig() {
        this.workingMode = WorkingMode.ECB;
        this.paddingMode = PaddingMode.PKCS5_PADDING;
        this.useGeneratorKey = true;
        this.generatorKeyLength = CryptoConst.AES_KEY_LENGTH;
    }

}
