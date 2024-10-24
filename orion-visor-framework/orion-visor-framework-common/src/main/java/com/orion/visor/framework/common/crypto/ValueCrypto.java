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
package com.orion.visor.framework.common.crypto;

import com.orion.lang.utils.codec.Base62s;
import com.orion.lang.utils.crypto.symmetric.SymmetricCrypto;

/**
 * 数据加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:20
 */
public interface ValueCrypto extends SymmetricCrypto {

    /**
     * 初始化
     */
    void init();

    /**
     * 加密后 base62 编码
     *
     * @param plain 明文
     * @return 密文
     */
    default String encryptBase62(String plain) {
        return new String(Base62s.encode(this.encrypt(plain)));
    }

    /**
     * base62 解码后解密
     *
     * @param text 密文
     * @return 明文
     */
    default String decryptBase62(String text) {
        return new String(this.decrypt(Base62s.decode(text)));
    }

}
