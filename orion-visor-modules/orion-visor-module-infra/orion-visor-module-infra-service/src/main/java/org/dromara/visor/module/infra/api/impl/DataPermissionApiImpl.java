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

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.convert.DataPermissionProviderConvert;
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
    public void addDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto) {
        // 校验参数
        List<Long> relIdList = dto.getRelIdList();
        Valid.isTrue(dto.getUserId() != null || dto.getRoleId() != null);
        Valid.notEmpty(relIdList);
        // 添加权限
        DataPermissionUpdateRequest request = DataPermissionProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        dataPermissionService.addDataPermission(request);
    }

    @Override
    public void updateDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto) {
        // 校验参数
        Valid.isTrue(dto.getUserId() != null || dto.getRoleId() != null);
        // 修改权限
        DataPermissionUpdateRequest request = DataPermissionProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        dataPermissionService.updateDataPermission(request);
    }

    @Override
    public boolean hasPermission(DataPermissionTypeEnum type, Long userId, Long relId) {
        Valid.allNotNull(userId, relId);
        return dataPermissionService.hasPermission(type.name(), userId, relId);
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
