package com.orion.ops.framework.common.utils;

import com.orion.lang.utils.crypto.symmetric.SymmetricCrypto;

/**
 * 加密工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:05
 */
public class CryptoUtils {

    /**
     * 加密器 供 framework 赋值
     */
    public static SymmetricCrypto delegate;

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    public static byte[] encrypt(byte[] plain) {
        return delegate.encrypt(plain);
    }

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    public static byte[] encrypt(String plain) {
        return delegate.encrypt(plain);
    }

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    public static String encryptAsString(String plain) {
        return delegate.encryptAsString(plain);
    }

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    public static String encryptAsString(byte[] plain) {
        return delegate.encryptAsString(plain);
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    public static byte[] decrypt(byte[] text) {
        return delegate.decrypt(text);
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    public static byte[] decrypt(String text) {
        return delegate.decrypt(text);
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    public static String decryptAsString(String text) {
        return delegate.decryptAsString(text);
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    public static String decryptAsString(byte[] text) {
        return delegate.decryptAsString(text);
    }

    /**
     * 验证加密结果
     *
     * @param plain 明文
     * @param text  密文
     * @return 是否成功
     */
    public static boolean verify(String plain, String text) {
        return delegate.verify(plain, text);
    }

    /**
     * 验证加密结果
     *
     * @param plain 明文
     * @param text  密文
     * @return 是否成功
     */
    public static boolean verify(byte[] plain, byte[] text) {
        return delegate.verify(plain, text);
    }

}
