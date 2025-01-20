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
package org.dromara.visor.common.interfaces;

import cn.orionsec.kit.lang.utils.codec.Base62s;
import cn.orionsec.kit.lang.utils.crypto.symmetric.SymmetricCrypto;

/**
 * aes 加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:20
 */
public interface AesEncryptor extends SymmetricCrypto {

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
