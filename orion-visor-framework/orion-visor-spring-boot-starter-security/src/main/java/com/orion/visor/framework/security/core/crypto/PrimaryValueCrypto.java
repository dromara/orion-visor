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

import com.orion.lang.utils.Exceptions;
import com.orion.visor.framework.common.crypto.ValueCrypto;

/**
 * 默认加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/21 12:11
 */
public class PrimaryValueCrypto implements ValueCrypto {

    private static ValueCrypto delegate;

    @Override
    public void init() {
    }

    @Override
    public byte[] encrypt(byte[] plain) {
        return delegate.encrypt(plain);
    }

    @Override
    public byte[] decrypt(byte[] text) {
        return delegate.decrypt(text);
    }

    protected static void setDelegate(ValueCrypto delegate) {
        if (PrimaryValueCrypto.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        PrimaryValueCrypto.delegate = delegate;
    }

}
