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

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.dto.host.HostVncConfigDTO;
import org.dromara.visor.module.asset.enums.HostAuthTypeEnum;
import org.dromara.visor.module.asset.enums.HostIdentityTypeEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主机 VNC 配置策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 14:26
 */
@Component
public class HostVncConfigStrategy extends AbstractHostConfigStrategy<HostVncConfigDTO> {

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    public HostVncConfigStrategy() {
        super(HostVncConfigDTO.class);
    }

    @Override
    public HostVncConfigDTO getDefault() {
        return HostVncConfigDTO.builder()
                .port(5900)
                .username(Const.ROOT)
                .authType(HostAuthTypeEnum.PASSWORD.name())
                .noUsername(false)
                .noPassword(false)
                .clipboardEncoding(Const.UTF_8)
                .build();
    }

    @Override
    protected void preValid(HostVncConfigDTO model) {
        // 验证剪切板编码格式
        String clipboardEncoding = model.getClipboardEncoding();
        if (!Strings.isBlank(clipboardEncoding)) {
            this.validCharset(clipboardEncoding);
        }
        // 检查主机身份是否存在
        Long identityId = model.getIdentityId();
        if (identityId != null) {
            HostIdentityDO identity = Valid.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
            Valid.eq(HostIdentityTypeEnum.PASSWORD.name(), identity.getType(), ErrorMessage.CHECK_IDENTITY_PASSWORD);
        }
    }

    @Override
    protected void valid(HostVncConfigDTO model) {
        // 验证填充后的参数
        Valid.valid(model);
    }

    @Override
    protected void updateFill(HostVncConfigDTO beforeModel, HostVncConfigDTO afterModel) {
        // 无密码设置认证方式为密码验证
        if (Booleans.isTrue(afterModel.getNoPassword())) {
            afterModel.setAuthType(HostAuthTypeEnum.PASSWORD.name());
        }
        // 加密密码
        this.checkEncryptPassword(afterModel.getAuthType(), beforeModel, afterModel);
        afterModel.setHasPassword(null);
        afterModel.setUseNewPassword(null);
    }

    @Override
    public HostVncConfigDTO parse(String serialModel) {
        return HostTypeEnum.VNC.parse(serialModel);
    }

    @Override
    public void toView(HostVncConfigDTO model) {
        if (model == null) {
            return;
        }
        model.setHasPassword(Strings.isNotBlank(model.getPassword()));
        model.setPassword(null);
    }

}
