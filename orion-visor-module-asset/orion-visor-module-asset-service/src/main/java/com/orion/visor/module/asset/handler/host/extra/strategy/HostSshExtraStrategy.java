package com.orion.visor.module.asset.handler.host.extra.strategy;

import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.handler.data.strategy.MapDataStrategy;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.dao.HostIdentityDAO;
import com.orion.visor.module.asset.dao.HostKeyDAO;
import com.orion.visor.module.asset.enums.HostExtraSshAuthTypeEnum;
import com.orion.visor.module.asset.handler.host.extra.model.HostSshExtraModel;
import com.orion.visor.module.infra.api.DataPermissionApi;
import com.orion.visor.module.infra.enums.DataPermissionTypeEnum;
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
public class HostSshExtraStrategy implements MapDataStrategy<HostSshExtraModel> {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Override
    public HostSshExtraModel getDefault() {
        return HostSshExtraModel.builder()
                .authType(HostExtraSshAuthTypeEnum.DEFAULT.name())
                .build();
    }

    @Override
    public void updateFill(HostSshExtraModel beforeModel, HostSshExtraModel afterModel) {
    }

    @Override
    public void preValid(HostSshExtraModel model) {
        HostExtraSshAuthTypeEnum authType = Valid.valid(HostExtraSshAuthTypeEnum::of, model.getAuthType());
        model.setAuthType(authType.name());
        Long keyId = model.getKeyId();
        Long identityId = model.getIdentityId();
        // 必填验证
        if (HostExtraSshAuthTypeEnum.CUSTOM_KEY.equals(authType)) {
            Valid.notNull(keyId);
        } else if (HostExtraSshAuthTypeEnum.CUSTOM_IDENTITY.equals(authType)) {
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

    @Override
    public void valid(HostSshExtraModel model) {
    }

}
