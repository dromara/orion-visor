package com.orion.ops.module.asset.service.impl;

import com.orion.lang.function.Functions;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.utils.TreeUtils;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.HostGroupConvert;
import com.orion.ops.module.asset.entity.request.asset.AssetAuthorizedDataQueryRequest;
import com.orion.ops.module.asset.entity.vo.*;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import com.orion.ops.module.asset.service.HostIdentityService;
import com.orion.ops.module.asset.service.HostKeyService;
import com.orion.ops.module.asset.service.HostService;
import com.orion.ops.module.infra.api.DataGroupApi;
import com.orion.ops.module.infra.api.DataGroupRelApi;
import com.orion.ops.module.infra.api.DataPermissionApi;
import com.orion.ops.module.infra.api.SystemUserApi;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资产模块 授权数据服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/30 18:35
 */
@Slf4j
@Service
public class AssetAuthorizedDataServiceImpl implements AssetAuthorizedDataService {

    @Resource
    private DataGroupApi dataGroupApi;

    @Resource
    private DataGroupRelApi dataGroupRelApi;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Resource
    private SystemUserApi systemUserApi;

    @Resource
    private HostService hostService;

    @Resource
    private HostKeyService hostKeyService;

    @Resource
    private HostIdentityService hostIdentityService;

    @Override
    public List<Long> getAuthorizedDataRelId(DataPermissionTypeEnum type, AssetAuthorizedDataQueryRequest request) {
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();
        Valid.isTrue(userId != null || roleId != null);
        if (userId != null) {
            // 查询用户数据
            return dataPermissionApi.getRelIdListByUserId(type, userId);
        } else {
            // 查询角色数据
            return dataPermissionApi.getRelIdListByRoleId(type, roleId);
        }
    }

    @Override
    public AuthorizedHostGroupWrapperVO getUserAuthorizedHostGroup(Long userId) {
        if (systemUserApi.isAdminUser(userId)) {
            // 管理员查询所有
            return this.buildUserAuthorizedHostGroup(null);
        } else {
            // 其他用户 查询授权的数据
            List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_GROUP, userId);
            if (authorizedIdList.isEmpty()) {
                // 无数据
                return AuthorizedHostGroupWrapperVO.builder()
                        .groupTree(Lists.empty())
                        .hostList(Lists.empty())
                        .build();
            }
            return this.buildUserAuthorizedHostGroup(authorizedIdList);
        }
    }

    @Override
    public List<HostKeyVO> getUserAuthorizedHostKey(Long userId) {
        if (systemUserApi.isAdminUser(userId)) {
            // 管理员查询所有
            return hostKeyService.getHostKeyList();
        } else {
            // 其他用户 查询授权的数据
            List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_KEY, userId);
            if (authorizedIdList.isEmpty()) {
                return Lists.empty();
            }
            // 映射数据
            Map<Long, HostKeyVO> keys = hostKeyService.getHostKeyList()
                    .stream()
                    .collect(Collectors.toMap(HostKeyVO::getId, Function.identity(), Functions.right()));
            return authorizedIdList.stream()
                    .map(keys::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<HostIdentityVO> getUserAuthorizedHostIdentity(Long userId) {
        if (systemUserApi.isAdminUser(userId)) {
            // 管理员查询所有
            return hostIdentityService.getHostIdentityList();
        } else {
            // 其他用户 查询授权的数据
            List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_IDENTITY, userId);
            if (authorizedIdList.isEmpty()) {
                return Lists.empty();
            }
            // 映射数据
            Map<Long, HostIdentityVO> identities = hostIdentityService.getHostIdentityList()
                    .stream()
                    .collect(Collectors.toMap(HostIdentityVO::getId, Function.identity(), Functions.right()));
            return authorizedIdList.stream()
                    .map(identities::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 构建授权的主机分组树
     *
     * @param authorizedGroupIdList authorizedGroupIdList
     * @return tree
     */
    private AuthorizedHostGroupWrapperVO buildUserAuthorizedHostGroup(List<Long> authorizedGroupIdList) {
        final boolean allData = Lists.isEmpty(authorizedGroupIdList);
        AuthorizedHostGroupWrapperVO wrapper = new AuthorizedHostGroupWrapperVO();
        // 查询主机列表
        List<HostVO> hosts = hostService.getHostListByCache();
        Map<Long, HostVO> hostMap = hosts.stream()
                .collect(Collectors.toMap(HostVO::getId, Function.identity(), Functions.right()));
        // 查询分组引用
        Map<Long, Set<Long>> groupRel = dataGroupRelApi.getGroupRelList(DataGroupTypeEnum.HOST);
        // 查询分组
        List<DataGroupDTO> dataGroup = dataGroupApi.getDataGroupList(DataGroupTypeEnum.HOST);
        // 过滤分组
        if (!allData) {
            // 构建已授权的分组
            List<DataGroupDTO> relNodes = new ArrayList<>();
            TreeUtils.getAllNodes(dataGroup, authorizedGroupIdList, relNodes);
            dataGroup = new ArrayList<>(new HashSet<>(relNodes));
        }
        // 设置组内数据
        List<HostGroupTreeVO> groupList = HostGroupConvert.MAPPER.toList(dataGroup);
        groupList.stream()
                // 因为可能父菜单没有授权 这里需要判断组
                .filter(s -> allData || authorizedGroupIdList.contains(s.getId()))
                .forEach(s -> {
                    List<HostVO> groupHosts = Lists.stream(groupRel.get(s.getId()))
                            .map(hostMap::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    s.setHostList(groupHosts);
                });
        // 构建主机树
        HostGroupTreeVO rootNode = HostGroupTreeVO.builder()
                .id(Const.ROOT_PARENT_ID)
                .sort(Const.DEFAULT_SORT)
                .build();
        TreeUtils.buildGroupTree(rootNode, groupList);
        wrapper.setGroupTree(rootNode.getChildren());
        // 设置授权的主机
        if (allData) {
            // 设置全部数据
            wrapper.setHostList(hosts);
        } else {
            // 仅设置已授权的数据
            List<HostVO> groupHosts = groupList.stream()
                    .filter(s -> authorizedGroupIdList.contains(s.getId()))
                    .map(s -> groupRel.get(s.getId()))
                    .filter(Lists::isNoneEmpty)
                    .flatMap(Collection::stream)
                    .map(hostMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            wrapper.setHostList(groupHosts);
        }
        return wrapper;
    }

}
