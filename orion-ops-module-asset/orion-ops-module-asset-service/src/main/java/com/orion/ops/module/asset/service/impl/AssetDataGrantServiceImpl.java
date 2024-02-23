package com.orion.ops.module.asset.service.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.dao.HostIdentityDAO;
import com.orion.ops.module.asset.dao.HostKeyDAO;
import com.orion.ops.module.asset.entity.domain.HostIdentityDO;
import com.orion.ops.module.asset.entity.domain.HostKeyDO;
import com.orion.ops.module.asset.entity.request.asset.AssetDataGrantRequest;
import com.orion.ops.module.asset.service.AssetDataGrantService;
import com.orion.ops.module.infra.api.DataGroupApi;
import com.orion.ops.module.infra.api.DataPermissionApi;
import com.orion.ops.module.infra.api.SystemRoleApi;
import com.orion.ops.module.infra.api.SystemUserApi;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.entity.dto.data.DataPermissionUpdateDTO;
import com.orion.ops.module.infra.entity.dto.role.SystemRoleDTO;
import com.orion.ops.module.infra.entity.dto.user.SystemUserDTO;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产模块 数据授权服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/30 18:34
 */
@Slf4j
@Service
public class AssetDataGrantServiceImpl implements AssetDataGrantService {

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Resource
    private SystemRoleApi systemRoleApi;

    @Resource
    private SystemUserApi systemUserApi;

    @Resource
    private DataGroupApi dataGroupApi;

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantHostGroup(AssetDataGrantRequest request) {
        // 检查身份
        this.checkGrantIdentity(request);
        // 检查数据是否存在
        List<Long> idList = request.getIdList();
        if (!Lists.isEmpty(idList)) {
            List<DataGroupDTO> groupList = dataGroupApi.getByIdList(idList);
            Valid.eq(groupList.size(), idList.size(), ErrorMessage.DATA_MODIFIED);
        }
        // 数据授权
        SpringHolder.getBean(AssetDataGrantService.class)
                .grantData(DataPermissionTypeEnum.HOST_GROUP, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantHostKey(AssetDataGrantRequest request) {
        // 检查身份
        this.checkGrantIdentity(request);
        // 检查数据是否存在
        List<Long> idList = request.getIdList();
        if (!Lists.isEmpty(idList)) {
            List<HostKeyDO> keys = hostKeyDAO.selectBatchIds(idList);
            Valid.eq(keys.size(), idList.size(), ErrorMessage.DATA_MODIFIED);
        }
        // 数据授权
        SpringHolder.getBean(AssetDataGrantService.class)
                .grantData(DataPermissionTypeEnum.HOST_KEY, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantHostIdentity(AssetDataGrantRequest request) {
        // 检查身份
        this.checkGrantIdentity(request);
        // 检查数据是否存在
        List<Long> idList = request.getIdList();
        if (!Lists.isEmpty(idList)) {
            List<HostIdentityDO> identities = hostIdentityDAO.selectBatchIds(idList);
            Valid.eq(identities.size(), idList.size(), ErrorMessage.DATA_MODIFIED);
        }
        // 数据授权
        SpringHolder.getBean(AssetDataGrantService.class)
                .grantData(DataPermissionTypeEnum.HOST_IDENTITY, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantData(DataPermissionTypeEnum type, AssetDataGrantRequest request) {
        // 授权
        DataPermissionUpdateDTO grant = DataPermissionUpdateDTO.builder()
                .roleId(request.getRoleId())
                .userId(request.getUserId())
                .relIdList(request.getIdList())
                .build();
        dataPermissionApi.updateDataPermission(type, grant);
    }

    /**
     * 检查授权身份
     *
     * @param request request
     */
    private void checkGrantIdentity(AssetDataGrantRequest request) {
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();
        Valid.isTrue(userId != null || roleId != null);
        if (userId != null) {
            // 检测用户是否存在
            SystemUserDTO user = systemUserApi.getUserById(userId);
            Valid.notNull(user, ErrorMessage.USER_ABSENT);
            OperatorLogs.add(OperatorLogs.GRANT_TYPE, Const.CN_USER);
            OperatorLogs.add(OperatorLogs.GRANT_NAME, user.getNickname() + "(" + user.getUsername() + ")");
        }
        if (roleId != null) {
            // 检测角色是否存在
            SystemRoleDTO role = systemRoleApi.getRoleById(roleId);
            Valid.notNull(role, ErrorMessage.ROLE_ABSENT);
            OperatorLogs.add(OperatorLogs.GRANT_TYPE, Const.CN_ROLE);
            OperatorLogs.add(OperatorLogs.GRANT_NAME, role.getName() + "(" + role.getCode() + ")");
        }
    }

}
