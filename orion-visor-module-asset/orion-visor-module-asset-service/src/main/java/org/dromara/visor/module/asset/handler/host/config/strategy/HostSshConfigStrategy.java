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
package org.dromara.visor.module.asset.handler.host.config.strategy;

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Charsets;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.framework.common.security.PasswordModifier;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.enums.HostSshAuthTypeEnum;
import org.dromara.visor.module.asset.enums.HostSshOsTypeEnum;
import org.dromara.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主机 SSH 配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 14:26
 */
@Component
public class HostSshConfigStrategy extends AbstractGenericsDataStrategy<HostSshConfigModel> {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    private static final String USERNAME = "root";

    public HostSshConfigStrategy() {
        super(HostSshConfigModel.class);
    }

    @Override
    public HostSshConfigModel getDefault() {
        return HostSshConfigModel.builder()
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
    protected void preValid(HostSshConfigModel model) {
        // 验证编码格式
        this.validCharset(model.getCharset());
        this.validCharset(model.getFileNameCharset());
        this.validCharset(model.getFileContentCharset());
        // 检查主机密钥是否存在
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
    protected void valid(HostSshConfigModel model) {
        // 验证填充后的参数
        Valid.valid(model);
    }

    @Override
    protected void updateFill(HostSshConfigModel beforeModel, HostSshConfigModel afterModel) {
        // 加密密码
        this.checkEncryptPassword(beforeModel, afterModel);
        afterModel.setHasPassword(null);
        afterModel.setUseNewPassword(null);
    }

    @Override
    public void toView(HostSshConfigModel model) {
        if (model == null) {
            return;
        }
        model.setHasPassword(Strings.isNotBlank(model.getPassword()));
        model.setPassword(null);
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