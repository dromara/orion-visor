/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.handler.host.config;

import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.entity.dto.host.HostSshConfigDTO;
import org.dromara.visor.module.asset.enums.HostAuthTypeEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
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
public class HostSshConfigStrategy extends AbstractHostConfigStrategy<HostSshConfigDTO> {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    public HostSshConfigStrategy() {
        super(HostSshConfigDTO.class);
    }

    @Override
    public HostSshConfigDTO getDefault() {
        return HostSshConfigDTO.builder()
                .port(22)
                .username(Const.ROOT)
                .authType(HostAuthTypeEnum.PASSWORD.name())
                .connectTimeout(Const.MS_S_10)
                .charset(Const.UTF_8)
                .fileNameCharset(Const.UTF_8)
                .fileContentCharset(Const.UTF_8)
                .build();
    }

    @Override
    protected void preValid(HostSshConfigDTO model) {
        // 验证编码格式
        this.validCharset(model.getCharset());
        this.validCharset(model.getFileNameCharset());
        this.validCharset(model.getFileContentCharset());
        // 检查主机密钥是否存在
        Long keyId = model.getKeyId();
        if (keyId != null) {
            Assert.notNull(hostKeyDAO.selectById(keyId), ErrorMessage.KEY_ABSENT);
        }
        // 检查主机身份是否存在
        Long identityId = model.getIdentityId();
        if (identityId != null) {
            Assert.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
        }
    }

    @Override
    protected void valid(HostSshConfigDTO model) {
        // 验证填充后的参数
        Assert.valid(model);
    }

    @Override
    protected void updateFill(HostSshConfigDTO beforeModel, HostSshConfigDTO afterModel) {
        // 加密密码
        this.checkEncryptPassword(afterModel.getAuthType(), beforeModel, afterModel);
        afterModel.setHasPassword(null);
        afterModel.setUseNewPassword(null);
    }

    @Override
    public HostSshConfigDTO parse(String serialModel) {
        return HostTypeEnum.SSH.parse(serialModel);
    }

    @Override
    public void toView(HostSshConfigDTO model) {
        if (model == null) {
            return;
        }
        model.setHasPassword(Strings.isNotBlank(model.getPassword()));
        model.setPassword(null);
    }

}
