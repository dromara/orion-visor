package com.orion.ops.module.asset.handler.host.config.strategy;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.utils.CryptoUtils;
import com.orion.ops.module.asset.handler.host.config.model.HostSshConfigModel;

import java.util.Map;

/**
 * 主机 SSH 配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 14:26
 */
public class HostSshConfigStrategy implements HostConfigStrategy<HostSshConfigModel> {

    private static final int SSH_PORT = 22;

    @Override
    public HostSshConfigModel getDefault() {
        return HostSshConfigModel.builder()
                .port(SSH_PORT)
                .charset(Const.UTF_8)
                .connectTimeout(Const.MS_S_10)
                .fileNameCharset(Const.UTF_8)
                .fileContentCharset(Const.UTF_8)
                .build();
    }

    @Override
    public void updateFill(HostSshConfigModel before, HostSshConfigModel after) {
        // 加密密码
        this.checkEncryptPassword(after);
    }

    @Override
    public Map<String, Object> toView(String config) {
        if (config == null) {
            return null;
        }
        HostSshConfigModel model = JSON.parseObject(config, HostSshConfigModel.class);
        model.setPassword(null);
        return JSON.parseObject(JSON.toJSONString(model));
    }

    private void checkEncryptPassword(HostSshConfigModel config) {
        String password = config.getPassword();
        if (Strings.isBlank(password)) {
            return;
        }
        // 加密密码
        config.setPassword(CryptoUtils.encryptAsString(password));
    }

}
