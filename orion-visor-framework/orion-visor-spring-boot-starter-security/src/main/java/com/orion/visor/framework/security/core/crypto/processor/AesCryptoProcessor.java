package com.orion.visor.framework.security.core.crypto.processor;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.crypto.Keys;
import com.orion.lang.utils.crypto.enums.CipherAlgorithm;
import com.orion.lang.utils.crypto.enums.WorkingMode;
import com.orion.lang.utils.crypto.symmetric.SymmetricBuilder;
import com.orion.lang.utils.crypto.symmetric.SymmetricCrypto;
import com.orion.visor.framework.security.configuration.config.AesCryptoConfig;
import com.orion.visor.framework.security.core.crypto.CryptoProcessor;

import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * aes 加密器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/8 0:05
 */
public class AesCryptoProcessor extends CryptoProcessor<AesCryptoConfig> {

    /**
     * 加密器
     */
    private SymmetricCrypto crypto;

    /**
     * 加密器构建器
     */
    private SymmetricBuilder builder;

    public AesCryptoProcessor(AesCryptoConfig config) {
        super(config);
    }

    @Override
    public void init() {
        // 创建构建器
        this.builder = SymmetricBuilder.aes()
                .workingMode(config.getWorkingMode())
                .paddingMode(config.getPaddingMode());
        // 初始化密钥
        this.initSecretKey();
        // 初始化参数规格
        this.initParamSpec();
        // 创建加密器
        this.builderCrypto();
    }

    @Override
    protected void initSecretKey() {
        SecretKey secretKey;
        if (config.isUseGeneratorKey()) {
            // 生成密钥
            secretKey = Keys.generatorKey(config.getSecretKey(), config.getGeneratorKeyLength(), CipherAlgorithm.AES);
        } else {
            // 获取密钥
            secretKey = Keys.getSecretKey(config.getSecretKey(), CipherAlgorithm.AES);
        }
        builder.secretKey(secretKey);
    }

    @Override
    protected void initParamSpec() {
        String iv = config.getIv();
        String gcm = config.getGcm();
        if (!Strings.isEmpty(iv)) {
            // 向量
            AlgorithmParameterSpec ivSpec = Keys.getIvSpec(CipherAlgorithm.AES, Strings.bytes(iv));
            builder.paramSpec(ivSpec);
        } else if (!Strings.isEmpty(gcm)) {
            // gcm
            AlgorithmParameterSpec gcmSpec = Keys.getGcmSpec(CipherAlgorithm.AES, Strings.bytes(gcm));
            builder.paramSpec(gcmSpec);
        }
    }

    @Override
    protected void builderCrypto() {
        // 设置 aad
        String aad = config.getAad();
        if (!Strings.isEmpty(aad)) {
            builder.aad(aad);
        }
        // 构建加密器
        if (WorkingMode.ECB.equals(config.getWorkingMode())) {
            // 无参数 ECB 模式
            this.crypto = builder.buildEcb();
        } else {
            // 有参数规格模式
            this.crypto = builder.buildParam();
        }
    }

    @Override
    public byte[] encrypt(byte[] plain) {
        return crypto.encrypt(plain);
    }

    @Override
    public byte[] decrypt(byte[] text) {
        return crypto.decrypt(text);
    }

}
