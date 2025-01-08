package org.dromara.visor.common.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.common.interfaces.RsaEncryptor;

/**
 * rsa 加密工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/5 21:13
 */
public class RsaEncryptorUtils {

    private static RsaEncryptor delegate;

    private RsaEncryptorUtils() {
    }

    /**
     * 加密
     *
     * @param value value
     * @return value
     */
    public static String encrypt(String value) {
        return delegate.encrypt(value);
    }

    /**
     * 解密
     *
     * @param value value
     * @return value
     */
    public static String decrypt(String value) {
        return delegate.decrypt(value);
    }

    public static void setDelegate(RsaEncryptor delegate) {
        if (RsaEncryptorUtils.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        RsaEncryptorUtils.delegate = delegate;
    }

}
