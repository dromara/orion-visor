package org.dromara.visor.common.interfaces;

/**
 * rsa 加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/5 20:58
 */
public interface RsaEncryptor {

    /**
     * 加密
     *
     * @param value value
     * @return value
     */
    String encrypt(String value);

    /**
     * 解密
     *
     * @param value value
     * @return value
     */
    String decrypt(String value);

}
