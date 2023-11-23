package com.orion.ops.module.asset.service.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.TreeUtils;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.HostGroupConvert;
import com.orion.ops.module.asset.entity.request.host.HostGroupGrantQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostGroupGrantRequest;
import com.orion.ops.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostGroupTreeVO;
import com.orion.ops.module.asset.service.HostGroupService;
import com.orion.ops.module.infra.api.*;
import com.orion.ops.module.infra.entity.dto.data.*;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private SystemRoleApi systemRoleApi;

    @Resource
    private SystemUserApi systemUserApi;

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

    @Override
    public List<Long> getAuthorizedHostGroup(HostGroupGrantQueryRequest request) {
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();
        Valid.isTrue(userId != null || roleId != null);
        if (userId != null) {
            // 查询用户数据
            return dataPermissionApi.getRelIdListByUserId(DataPermissionTypeEnum.HOST_GROUP, userId);
        } else {
            // 查询角色数据
            return dataPermissionApi.getRelIdListByRoleId(DataPermissionTypeEnum.HOST_GROUP, roleId);
        }
    }

    @Override
    public void grantHostGroup(HostGroupGrantRequest request) {
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();
        Valid.isTrue(userId != null || roleId != null);
        if (userId != null) {
            // 检测用户是否存在
            Valid.notNull(systemUserApi.getUserById(userId), ErrorMessage.USER_ABSENT);
        }
        if (roleId != null) {
            // 检测角色是否存在
            Valid.notNull(systemRoleApi.getRoleById(roleId), ErrorMessage.ROLE_ABSENT);
        }
        // 授权
        DataPermissionUpdateDTO grant = DataPermissionUpdateDTO.builder()
                .roleId(roleId)
                .userId(userId)
                .relIdList(request.getGroupIdList())
                .build();
        dataPermissionApi.updateDataPermission(DataPermissionTypeEnum.HOST_GROUP, grant);
    }

    @Override
    public List<HostGroupTreeVO> getUserAuthorizedHostGroup(Long userId) {
        if (systemUserApi.isAdminUser(userId)) {
            // 管理员查询所有
            return this.buildUserAuthorizedHostGroup(null);
        } else {
            // 其他用户查询授权的分组
            List<Long> authorizedGroupIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_GROUP, userId);
            if (authorizedGroupIdList.isEmpty()) {
                return Lists.empty();
            }
            return this.buildUserAuthorizedHostGroup(authorizedGroupIdList);
        }
    }

    /**
     * 构建授权的主机分组树
     *
     * @param authorizedGroupIdList authorizedGroupIdList
     * @return tree
     */
    private List<HostGroupTreeVO> buildUserAuthorizedHostGroup(List<Long> authorizedGroupIdList) {
        // 查询分组
        List<DataGroupDTO> dataGroup = dataGroupApi.getDataGroupList(DataGroupTypeEnum.HOST);
        // 过滤分组
        if (!Lists.isEmpty(authorizedGroupIdList)) {
            // 构建已授权的分组
            List<DataGroupDTO> relNodes = new ArrayList<>();
            TreeUtils.getAllNodes(dataGroup, authorizedGroupIdList, relNodes);
            dataGroup = new ArrayList<>(new HashSet<>(relNodes));
        }
        // 查询分组引用
        Map<Long, Set<Long>> groupRel = dataGroupRelApi.getGroupRelList(DataGroupTypeEnum.HOST);
        // 设置组内数据
        List<HostGroupTreeVO> groupList = HostGroupConvert.MAPPER.toList(dataGroup);
        if (Lists.isEmpty(authorizedGroupIdList)) {
            // 设置全部数据
            groupList.forEach(s -> s.setHosts(groupRel.get(s.getId())));
        } else {
            // 仅设置已授权的数据
            groupList.stream()
                    .filter(s -> authorizedGroupIdList.contains(s.getId()))
                    .forEach(s -> s.setHosts(groupRel.get(s.getId())));
        }
        // 构建树
        HostGroupTreeVO rootNode = HostGroupTreeVO.builder()
                .id(Const.ROOT_PARENT_ID)
                .sort(Const.DEFAULT_SORT)
                .build();
        TreeUtils.buildGroupTree(rootNode, groupList);
        return rootNode.getChildren();
    }

}
