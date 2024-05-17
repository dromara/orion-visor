package com.orion.visor.framework.security.core.crypto.aes;

import com.orion.lang.utils.crypto.CryptoConst;
import com.orion.lang.utils.crypto.enums.PaddingMode;
import com.orion.lang.utils.crypto.enums.WorkingMode;
import com.orion.visor.framework.security.core.crypto.CryptoConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * aes 加密器配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 22:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AesCryptoConfig extends CryptoConfig {

    /**
     * 加密模式
     */
    private WorkingMode workingMode = WorkingMode.ECB;

    /**
     * 填充模式
     */
    private PaddingMode paddingMode = PaddingMode.PKCS5_PADDING;

    /**
     * 加密密钥
     */
    private String secretKey;

    /**
     * 是否生成密钥
     */
    private boolean useGeneratorKey = true;

    /**
     * 生成的密钥长度 128 192 256bytes
     */
    private int generatorKeyLength = CryptoConst.AES_KEY_LENGTH;

    /**
     * 向量 长度为 16bytes
     * 除 ECB/GCM 外的工作模式
     */
    private String iv;

    /**
     * GCM 模式参数 长度为 96 104 112 120 128bytes
     */
    private String gcm;

    /**
     * GCM 模式 aad
     */
    private String aad;

}
