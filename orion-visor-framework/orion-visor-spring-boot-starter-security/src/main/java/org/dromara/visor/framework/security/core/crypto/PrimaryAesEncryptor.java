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
package org.dromara.visor.framework.security.core.crypto;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.common.interfaces.AesEncryptor;

/**
 * 默认加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/21 12:11
 */
public class PrimaryAesEncryptor implements AesEncryptor {

    private static AesEncryptor delegate;

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

    protected static void setDelegate(AesEncryptor delegate) {
        if (PrimaryAesEncryptor.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        PrimaryAesEncryptor.delegate = delegate;
    }

}
