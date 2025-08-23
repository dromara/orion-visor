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
import org.dromara.visor.common.constant.ConfigKeys;
import org.dromara.visor.common.cipher.RsaDecryptor;

import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * rsa 加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/7 11:32
 */
public class RsaDecryptorImpl implements RsaDecryptor {

    private static final String SPLIT = "\\|";

    private final ConfigRef<RSAPrivateKey> privateKey;

    public RsaDecryptorImpl(ConfigStore configStore) {
        this.privateKey = configStore.ref(ConfigKeys.ENCRYPT_PRIVATE_KEY, RSA::getPrivateKey);
    }

    @Override
    public String decrypt(String value) {
        return Arrays.stream(value.split(SPLIT))
                .map(s -> RSA.decrypt(s, privateKey.value))
                .collect(Collectors.joining());
    }

}
