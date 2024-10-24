/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.framework.security.core.crypto;

import com.orion.visor.framework.common.crypto.ValueCrypto;
import com.orion.visor.framework.common.utils.CryptoUtils;

/**
 * 数据加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 22:48
 */
public abstract class CryptoProcessor<Config extends CryptoConfig> implements ValueCrypto {

    protected final Config config;

    protected CryptoProcessor(Config config) {
        this.config = config;
        // 设置为默认加密器
        if (config.isPrimary()) {
            PrimaryValueCrypto.setDelegate(this);
            CryptoUtils.setDelegate(this);
        }
    }

    /**
     * 初始化密钥
     */
    protected abstract void initSecretKey();

    /**
     * 初始化参数规格
     */
    protected abstract void initParamSpec();

    /**
     * 构建加密器
     */
    protected abstract void builderCrypto();

}
