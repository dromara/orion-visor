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
