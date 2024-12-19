/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import org.dromara.visor.module.asset.convert.HostGroupConvert;
import org.dromara.visor.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostGroupTreeVO;
import org.dromara.visor.module.asset.service.HostGroupService;
import org.dromara.visor.module.infra.api.DataGroupApi;
import org.dromara.visor.module.infra.api.DataGroupRelApi;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 主机分组服务 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 10:59
 */
@Service
public class HostGroupServiceImpl implements HostGroupService {

    @Resource
    private DataGroupApi dataGroupApi;

    @Resource
    private DataGroupRelApi dataGroupRelApi;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Override
    public Long createHostGroup(DataGroupCreateDTO request) {
        return dataGroupApi.createDataGroup(DataGroupTypeEnum.HOST, request);
    }

    @Override
    public List<HostGroupTreeVO> queryHostGroupTree() {
        List<DataGroupDTO> rows = dataGroupApi.getDataGroupTree(DataGroupTypeEnum.HOST);
        return HostGroupConvert.MAPPER.toList(rows);
    }

    @Override
    public Integer updateHostGroupName(DataGroupRenameDTO request) {
        return dataGroupApi.renameDataGroup(request);
    }

    @Override
    public Integer moveHostGroup(DataGroupMoveDTO request) {
        return dataGroupApi.moveDataGroup(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteHostGroup(Long id) {
        // 删除主机分组
        Integer effect = dataGroupApi.deleteDataGroupById(id);
        // 删除数据权限
        dataPermissionApi.deleteByRelId(DataPermissionTypeEnum.HOST_GROUP, id);
        return effect;
    }

    @Override
    public Set<Long> queryHostGroupRel(Long groupId) {
        return dataGroupRelApi.getGroupRelIdByGroupId(DataGroupTypeEnum.HOST, groupId);
    }

    @Override
    public void updateHostGroupRel(HostGroupRelUpdateRequest request) {
        dataGroupRelApi.updateGroupRel(request.getGroupId(), request.getHostIdList());
    }

}
