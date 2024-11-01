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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.request.asset.AssetDataGrantRequest;
import org.dromara.visor.module.asset.service.AssetDataGrantService;
import org.dromara.visor.module.infra.api.DataGroupApi;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.api.SystemRoleApi;
import org.dromara.visor.module.infra.api.SystemUserApi;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataPermissionUpdateDTO;
import org.dromara.visor.module.infra.entity.dto.role.SystemRoleDTO;
import org.dromara.visor.module.infra.entity.dto.user.SystemUserDTO;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
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
            idList.clear();
            idList.addAll(Lists.map(groupList, DataGroupDTO::getId));
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
            idList.clear();
            idList.addAll(Lists.map(keys, HostKeyDO::getId));
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
            idList.clear();
            idList.addAll(Lists.map(identities, HostIdentityDO::getId));
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
