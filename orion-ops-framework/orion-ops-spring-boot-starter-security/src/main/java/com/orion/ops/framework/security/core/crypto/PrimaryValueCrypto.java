package com.orion.ops.framework.security.core.crypto;

import com.orion.ops.framework.common.crypto.ValueCrypto;

/**
 * 默认加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/21 12:11
 */
public class PrimaryValueCrypto implements ValueCrypto {

    protected static ValueCrypto delegate;

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

}
