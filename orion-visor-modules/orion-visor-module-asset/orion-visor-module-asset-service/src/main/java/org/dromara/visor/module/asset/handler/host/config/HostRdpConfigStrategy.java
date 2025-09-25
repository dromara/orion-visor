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
import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.dto.host.HostRdpConfigDTO;
import org.dromara.visor.module.asset.enums.HostAuthTypeEnum;
import org.dromara.visor.module.asset.enums.HostIdentityTypeEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主机 RDP 配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 14:26
 */
@Component
public class HostRdpConfigStrategy extends AbstractHostConfigStrategy<HostRdpConfigDTO> {

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    public HostRdpConfigStrategy() {
        super(HostRdpConfigDTO.class);
    }

    @Override
    public HostRdpConfigDTO getDefault() {
        return HostRdpConfigDTO.builder()
                .port(3389)
                .username(Const.ADMINISTRATOR)
                .authType(HostAuthTypeEnum.PASSWORD.name())
                .versionGt81(true)
                .timezone("Asia/Shanghai")
                .keyboardLayout("en-us-qwerty")
                .clipboardNormalize("preserve")
                .build();
    }

    @Override
    protected void preValid(HostRdpConfigDTO model) {
        // 检查主机身份是否存在
        Long identityId = model.getIdentityId();
        if (identityId != null) {
            HostIdentityDO identity = Assert.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
            Assert.eq(HostIdentityTypeEnum.PASSWORD.name(), identity.getType(), ErrorMessage.CHECK_IDENTITY_PASSWORD);
        }
    }

    @Override
    protected void valid(HostRdpConfigDTO model) {
        // 验证填充后的参数
        Assert.valid(model);
    }

    @Override
    protected void updateFill(HostRdpConfigDTO beforeModel, HostRdpConfigDTO afterModel) {
        // 加密密码
        this.checkEncryptPassword(afterModel.getAuthType(), beforeModel, afterModel);
        afterModel.setHasPassword(null);
        afterModel.setUseNewPassword(null);
    }

    @Override
    public HostRdpConfigDTO parse(String serialModel) {
        return HostTypeEnum.RDP.parse(serialModel);
    }

    @Override
    public void toView(HostRdpConfigDTO model) {
        if (model == null) {
            return;
        }
        model.setHasPassword(Strings.isNotBlank(model.getPassword()));
        model.setPassword(null);
    }

}
