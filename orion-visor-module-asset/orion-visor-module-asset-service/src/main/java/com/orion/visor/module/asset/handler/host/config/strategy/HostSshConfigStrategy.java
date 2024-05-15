package com.orion.visor.module.asset.handler.host.config.strategy;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Charsets;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.visor.framework.common.security.PasswordModifier;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.asset.dao.HostIdentityDAO;
import com.orion.visor.module.asset.dao.HostKeyDAO;
import com.orion.visor.module.asset.enums.HostSshAuthTypeEnum;
import com.orion.visor.module.asset.enums.HostSshOsTypeEnum;
import com.orion.visor.module.asset.handler.host.config.model.HostSshConfigModel;
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
                .authType(HostSshAuthTypeEnum.PASSWORD.name())
                .osType(HostSshOsTypeEnum.LINUX.name())
                .connectTimeout(Const.MS_S_10)
                .charset(Const.UTF_8)
                .fileNameCharset(Const.UTF_8)
                .fileContentCharset(Const.UTF_8)
                .build();
    }

    @Override
    public void preValid(HostSshConfigModel model) {
        // 验证认证类型
        Valid.valid(HostSshAuthTypeEnum::of, model.getAuthType());
        // 验证系统版本
        Valid.valid(HostSshOsTypeEnum::of, model.getOsType());
        // 验证编码格式
        this.validCharset(model.getCharset());
        this.validCharset(model.getFileNameCharset());
        this.validCharset(model.getFileContentCharset());
        // 检查主机秘钥是否存在
        Long keyId = model.getKeyId();
        if (keyId != null) {
            Valid.notNull(hostKeyDAO.selectById(keyId), ErrorMessage.KEY_ABSENT);
        }
        // 检查主机身份是否存在
        Long identityId = model.getIdentityId();
        if (identityId != null) {
            Valid.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
        }
    }

    @Override
    public void valid(HostSshConfigModel model) {
        // 验证填充后的参数
        Valid.valid(model);
    }

    @Override
    public void updateFill(HostSshConfigModel beforeModel, HostSshConfigModel afterModel) {
        // 加密密码
        this.checkEncryptPassword(beforeModel, afterModel);
        afterModel.setHasPassword(null);
        afterModel.setUseNewPassword(null);
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
        if (!HostSshAuthTypeEnum.PASSWORD.name().equals(after.getAuthType())) {
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
