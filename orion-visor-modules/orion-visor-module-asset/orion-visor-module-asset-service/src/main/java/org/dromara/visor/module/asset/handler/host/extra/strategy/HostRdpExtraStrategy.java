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
package org.dromara.visor.module.asset.handler.host.extra.strategy;

import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.enums.HostExtraAuthTypeEnum;
import org.dromara.visor.module.asset.handler.host.extra.model.HostRdpExtraModel;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主机拓展信息 - rdp 模型处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:17
 */
@Component
public class HostRdpExtraStrategy extends AbstractGenericsDataStrategy<HostRdpExtraModel> {

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private DataPermissionApi dataPermissionApi;

    public HostRdpExtraStrategy() {
        super(HostRdpExtraModel.class);
    }

    @Override
    public HostRdpExtraModel getDefault() {
        return HostRdpExtraModel.builder()
                .authType(HostExtraAuthTypeEnum.DEFAULT.name())
                .lowBandwidthMode(false)
                .build();
    }

    @Override
    public void preValid(HostRdpExtraModel model) {
        HostExtraAuthTypeEnum authType = Assert.valid(HostExtraAuthTypeEnum::of, model.getAuthType());
        model.setAuthType(authType.name());
        Long identityId = model.getIdentityId();
        Long userId = SecurityUtils.getLoginUserId();
        // 必填验证
        if (HostExtraAuthTypeEnum.CUSTOM_IDENTITY.equals(authType)) {
            Assert.notNull(identityId);
            // 验证主机身份是否存在
            Assert.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
            // 验证主机身份是否有权限
            Assert.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_IDENTITY, userId, identityId),
                    ErrorMessage.ANY_NO_PERMISSION,
                    DataPermissionTypeEnum.HOST_IDENTITY.getPermissionName());
        }
    }

}
