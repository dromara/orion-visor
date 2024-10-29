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
package org.dromara.visor.framework.common.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.framework.common.crypto.ValueCrypto;

/**
 * 加密工具类
 * <p>
 * PrimaryValueCrypto 代理类工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:05
 */
public class CryptoUtils {

    private static ValueCrypto delegate;

    private CryptoUtils() {
    }

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

    /**
     * 加密后 base62 编码
     *
     * @param plain 明文
     * @return 密文
     */
    public static String encryptBase62(String plain) {
        return delegate.encryptBase62(plain);
    }

    /**
     * base62 解码后解密
     *
     * @param text 密文
     * @return 明文
     */
    public static String decryptBase62(String text) {
        return delegate.decryptBase62(text);
    }

    public static void setDelegate(ValueCrypto delegate) {
        if (CryptoUtils.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        CryptoUtils.delegate = delegate;
    }

}
