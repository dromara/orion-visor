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
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.enums.HostExtraAuthTypeEnum;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSshExtraModel;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主机拓展信息 - ssh 模型处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:17
 */
@Component
public class HostSshExtraStrategy extends AbstractGenericsDataStrategy<HostSshExtraModel> {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private DataPermissionApi dataPermissionApi;

    public HostSshExtraStrategy() {
        super(HostSshExtraModel.class);
    }

    @Override
    public HostSshExtraModel getDefault() {
        return HostSshExtraModel.builder()
                .authType(HostExtraAuthTypeEnum.DEFAULT.name())
                .build();
    }

    @Override
    public void preValid(HostSshExtraModel model) {
        HostExtraAuthTypeEnum authType = Valid.valid(HostExtraAuthTypeEnum::of, model.getAuthType());
        model.setAuthType(authType.name());
        Long keyId = model.getKeyId();
        Long identityId = model.getIdentityId();
        // 必填验证
        if (HostExtraAuthTypeEnum.CUSTOM_KEY.equals(authType)) {
            Valid.notNull(keyId);
        } else if (HostExtraAuthTypeEnum.CUSTOM_IDENTITY.equals(authType)) {
            Valid.notNull(identityId);
        }
        // 验证主机密钥是否存在
        if (keyId != null) {
            Valid.notNull(hostKeyDAO.selectById(keyId), ErrorMessage.KEY_ABSENT);
        }
        // 验证主机身份是否存在
        if (identityId != null) {
            Valid.notNull(hostIdentityDAO.selectById(identityId), ErrorMessage.IDENTITY_ABSENT);
        }
        Long userId = SecurityUtils.getLoginUserId();
        // 验证主机密钥是否有权限
        if (keyId != null) {
            Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_KEY, userId, keyId),
                    ErrorMessage.ANY_NO_PERMISSION,
                    DataPermissionTypeEnum.HOST_KEY.getPermissionName());
        }
        // 验证主机身份是否有权限
        if (identityId != null) {
            Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_IDENTITY, userId, identityId),
                    ErrorMessage.ANY_NO_PERMISSION,
                    DataPermissionTypeEnum.HOST_IDENTITY.getPermissionName());
        }
    }

}
