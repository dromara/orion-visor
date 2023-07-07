package com.orion.ops.framework.security.core.crypto;

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

}
