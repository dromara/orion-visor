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
package org.dromara.visor.module.infra.api.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.entity.dto.data.DataPermissionBatchUpdateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataPermissionUpdateDTO;
import org.dromara.visor.module.infra.entity.request.data.DataPermissionUpdateRequest;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.dromara.visor.module.infra.service.DataPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据权限 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Slf4j
@Service
public class DataPermissionApiImpl implements DataPermissionApi {

    @Resource
    private DataPermissionService dataPermissionService;

    @Override
    public void updateDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto) {
        Long userId = dto.getUserId();
        Long roleId = dto.getRoleId();
        // 校验参数
        Assert.isTrue(userId != null || roleId != null);
        // 修改权限
        DataPermissionUpdateRequest request = DataPermissionUpdateRequest.builder()
                .type(type.name())
                .relIdList(dto.getRelIdList())
                .build();
        if (userId != null) {
            request.setUserIdList(Lists.singleton(userId));
        }
        if (roleId != null) {
            request.setRoleIdList(Lists.singleton(roleId));
        }
        dataPermissionService.updateDataPermission(request);
    }

    @Override
    public void updateDataPermission(DataPermissionTypeEnum type, DataPermissionBatchUpdateDTO dto) {
        List<Long> userIdList = dto.getUserIdList();
        List<Long> roleIdList = dto.getRoleIdList();
        // 校验参数
        Assert.isTrue(Lists.isNotEmpty(userIdList) || Lists.isNotEmpty(roleIdList));
        // 修改权限
        DataPermissionUpdateRequest request = DataPermissionUpdateRequest.builder()
                .type(type.name())
                .relIdList(dto.getRelIdList())
                .userIdList(userIdList)
                .roleIdList(roleIdList)
                .build();
        dataPermissionService.updateDataPermission(request);
    }

    @Override
    public boolean hasPermission(DataPermissionTypeEnum type, Long userId, Long relId) {
        Assert.allNotNull(userId, relId);
        return dataPermissionService.hasPermission(type.name(), userId, relId);
    }

    @Override
    public List<Long> getUserIdListByRelId(DataPermissionTypeEnum type, Long relId) {
        return dataPermissionService.getUserIdListByRelId(type.name(), relId);
    }

    @Override
    public List<Long> getRoleIdListByRelId(DataPermissionTypeEnum type, Long relId) {
        return dataPermissionService.getRoleIdListByRelId(type.name(), relId);
    }

    @Override
    public List<Long> getRelIdListByUserId(DataPermissionTypeEnum type, Long userId) {
        return dataPermissionService.getRelIdListByUserId(type.name(), userId);
    }

    @Override
    public List<Long> getRelIdListByRoleId(DataPermissionTypeEnum type, Long roleId) {
        return dataPermissionService.getRelIdListByRoleId(type.name(), roleId);
    }

    @Override
    public List<Long> getUserAuthorizedRelIdList(DataPermissionTypeEnum type, Long userId) {
        return dataPermissionService.getUserAuthorizedRelIdList(type.name(), userId);
    }

    @Override
    public int deleteByRelId(DataPermissionTypeEnum type, Long relId) {
        return dataPermissionService.deleteByRelId(type.name(), relId);
    }

    @Override
    public int deleteByRelIdList(DataPermissionTypeEnum type, List<Long> relIdList) {
        return dataPermissionService.deleteByRelIdList(type.name(), relIdList);
    }

}
