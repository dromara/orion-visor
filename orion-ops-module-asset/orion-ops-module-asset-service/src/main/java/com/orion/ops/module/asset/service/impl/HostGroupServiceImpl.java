package com.orion.ops.module.asset.service.impl;

import com.orion.ops.module.asset.convert.HostGroupConvert;
import com.orion.ops.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostGroupTreeVO;
import com.orion.ops.module.asset.service.HostGroupService;
import com.orion.ops.module.infra.api.DataGroupApi;
import com.orion.ops.module.infra.api.DataGroupRelApi;
import com.orion.ops.module.infra.api.DataPermissionApi;
import com.orion.ops.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupMoveDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRenameDTO;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;
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
