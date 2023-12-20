package com.orion.ops.module.asset.handler.host.config.strategy;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Charsets;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.ops.framework.common.security.PasswordModifier;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.dao.HostIdentityDAO;
import com.orion.ops.module.asset.dao.HostKeyDAO;
import com.orion.ops.module.asset.enums.HostAuthTypeEnum;
import com.orion.ops.module.asset.handler.host.config.model.HostSshConfigModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 主机 SSH 配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 14:26
 */
@Component
public class HostSshConfigStrategy implements MapDataStrategy<HostSshConfigModel> {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    private static final int SSH_PORT = 22;

    private static final String USERNAME = "root";

    @Override
    public HostSshConfigModel getDefault() {
        return HostSshConfigModel.builder()
                .port(SSH_PORT)
                .username(USERNAME)
                .charset(Const.UTF_8)
                .connectTimeout(Const.MS_S_10)
                .fileNameCharset(Const.UTF_8)
                .fileContentCharset(Const.UTF_8)
                .build();
    }

    @Override
    public void preValidConfig(HostSshConfigModel config) {
        // 验证认证类型
        Valid.valid(HostAuthTypeEnum::of, config.getAuthType());
        // 验证编码格式
        this.validCharset(config.getCharset());
        this.validCharset(config.getFileNameCharset());
        this.validCharset(config.getFileContentCharset());
        // 检查主机秘钥是否存在
        Long keyId = config.getKeyId();
        if (keyId != null) {
            Valid.notNull(hostKeyDAO.selectById(keyId), ErrorMessage.KEY_ABSENT);
        }
        // 检查主机身份是否存在
        Long identityId = config.getIdentityId();
        if (identityId != null) {
            Valid.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
        }
    }

    @Override
    public void validConfig(HostSshConfigModel config) {
        // 验证填充后的参数
        Valid.valid(config);
    }

    @Override
    public void updateFill(HostSshConfigModel before, HostSshConfigModel after) {
        // 加密密码
        this.checkEncryptPassword(before, after);
        after.setHasPassword(null);
        after.setUseNewPassword(null);
    }

    @Override
    public Map<String, Object> toView(String config) {
        if (config == null) {
            return null;
        }
        HostSshConfigModel model = JSON.parseObject(config, HostSshConfigModel.class);
        model.setHasPassword(Strings.isNotBlank(model.getPassword()));
        model.setPassword(null);
        return JSON.parseObject(JSON.toJSONString(model));
    }

    /**
     * 检查加密密码
     *
     * @param before before
     * @param after  after
     */
    private void checkEncryptPassword(HostSshConfigModel before, HostSshConfigModel after) {
        // 非密码认证则直接赋值
        if (!HostAuthTypeEnum.PASSWORD.name().equals(after.getAuthType())) {
            after.setPassword(before.getPassword());
            return;
        }
        // 检查是否无密码
        if (Booleans.isTrue(after.getUseNewPassword()) && Strings.isBlank(after.getPassword())) {
            throw Exceptions.argument(ErrorMessage.PASSWORD_MISSING);
        }
        // 设置密码
        String newPassword = PasswordModifier.getEncryptNewPassword(after);
        if (newPassword == null) {
            newPassword = before.getPassword();
        }
        after.setPassword(newPassword);
    }

    /**
     * 检查编码格式
     *
     * @param charset charset
     */
    private void validCharset(String charset) {
        Valid.isTrue(Charsets.isSupported(charset), ErrorMessage.UNSUPPORTED_CHARSET, charset);
    }

}
