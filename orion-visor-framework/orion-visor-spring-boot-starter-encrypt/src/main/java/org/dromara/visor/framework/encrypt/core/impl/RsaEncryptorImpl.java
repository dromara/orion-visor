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
package org.dromara.visor.framework.encrypt.core.impl;

import cn.orionsec.kit.lang.utils.crypto.RSA;
import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;
import org.dromara.visor.common.interfaces.RsaEncryptor;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * rsa 加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/7 11:32
 */
public class RsaEncryptorImpl implements RsaEncryptor {

    private final ConfigRef<RSAPublicKey> publicKey;

    private final ConfigRef<RSAPrivateKey> privateKey;

    public RsaEncryptorImpl(ConfigStore configStore) {
        this.publicKey = configStore.ref("encrypt.publicKey", RSA::getPublicKey);
        this.privateKey = configStore.ref("encrypt.privateKey", RSA::getPrivateKey);
    }

    @Override
    public String encrypt(String value) {
        return RSA.encrypt(value, publicKey.value);
    }

    @Override
    public String decrypt(String value) {
        return RSA.decrypt(value, privateKey.value);
    }

}
