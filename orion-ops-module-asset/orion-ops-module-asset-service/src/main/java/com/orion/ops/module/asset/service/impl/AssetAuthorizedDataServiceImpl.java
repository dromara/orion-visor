package com.orion.ops.module.asset.service.impl;

import com.orion.lang.function.Functions;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.utils.TreeUtils;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.HostGroupConvert;
import com.orion.ops.module.asset.entity.request.asset.AssetAuthorizedDataQueryRequest;
import com.orion.ops.module.asset.entity.vo.*;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.service.*;
import com.orion.ops.module.infra.api.*;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.entity.dto.tag.TagDTO;
import com.orion.ops.module.infra.enums.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
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

    @Resource
    private HostConnectLogService hostConnectLogService;

    @Resource
    private FavoriteApi favoriteApi;

    @Resource
    private TagRelApi tagRelApi;

    @Resource
    private DataAliasApi dataAliasApi;

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
    public AuthorizedHostWrapperVO getUserAuthorizedHostGroup(Long userId) {
        if (systemUserApi.isAdminUser(userId)) {
            // 管理员查询所有
            return this.buildUserAuthorizedHostGroup(userId, null);
        } else {
            // 其他用户 查询授权的数据
            List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_GROUP, userId);
            if (authorizedIdList.isEmpty()) {
                // 无数据
                return AuthorizedHostWrapperVO.builder()
                        .groupTree(Lists.empty())
                        .hostList(Lists.empty())
                        .build();
            }
            return this.buildUserAuthorizedHostGroup(userId, authorizedIdList);
        }
    }

    @Override
    public List<Long> getUserAuthorizedHostId(Long userId) {
        // 查询授权的分组
        List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_GROUP, userId);
        if (authorizedIdList.isEmpty()) {
            return Lists.empty();
        }
        // 查询分组主机映射
        Map<Long, Set<Long>> dataGroupRel = dataGroupRelApi.getGroupRelList(DataGroupTypeEnum.HOST);
        // 返回
        return authorizedIdList.stream()
                .map(dataGroupRel::get)
                .filter(Lists::isNotEmpty)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
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
     * @param userId                userId
     * @param authorizedGroupIdList authorizedGroupIdList
     * @return tree
     */
    @SneakyThrows
    private AuthorizedHostWrapperVO buildUserAuthorizedHostGroup(Long userId, List<Long> authorizedGroupIdList) {
        final boolean allData = Lists.isEmpty(authorizedGroupIdList);
        AuthorizedHostWrapperVO wrapper = new AuthorizedHostWrapperVO();
        // 查询我的收藏
        Future<List<Long>> favoriteResult = favoriteApi.getFavoriteRelIdListAsync(FavoriteTypeEnum.HOST, userId);
        // 查询最近连接的主机
        Future<List<Long>> latestConnectHostIdList = hostConnectLogService.getLatestConnectHostIdAsync(HostConnectTypeEnum.SSH, userId);
        // 查询数据别名
        Future<Map<Long, String>> dataAliasResult = dataAliasApi.getDataAliasAsync(userId, DataExtraTypeEnum.HOST);
        // 查询分组
        List<DataGroupDTO> dataGroup = dataGroupApi.getDataGroupList(DataGroupTypeEnum.HOST);
        // 查询分组引用
        Map<Long, Set<Long>> dataGroupRel = dataGroupRelApi.getGroupRelList(DataGroupTypeEnum.HOST);
        // 过滤已经授权的分组
        if (!allData) {
            // 构建已授权的分组
            List<DataGroupDTO> relNodes = new ArrayList<>();
            TreeUtils.getAllNodes(dataGroup, authorizedGroupIdList, relNodes);
            dataGroup = new ArrayList<>(new HashSet<>(relNodes));
        }
        // 设置主机分组树
        wrapper.setGroupTree(this.getAuthorizedHostGroupTree(dataGroup));
        // 设置主机分组下的主机
        wrapper.setTreeNodes(this.getAuthorizedHostGroupNodes(allData,
                dataGroup,
                dataGroupRel,
                authorizedGroupIdList));
        // 设置已授权的所有主机
        wrapper.setHostList(this.getAuthorizedHostList(allData,
                dataGroup,
                dataGroupRel,
                authorizedGroupIdList));
        // 设置主机拓展信息
        this.getAuthorizedHostExtra(wrapper.getHostList(),
                favoriteResult.get(),
                dataAliasResult.get());
        // 设置最近连接的主机
        wrapper.setLatestHosts(latestConnectHostIdList.get());
        return wrapper;
    }

    /**
     * 构建主机分组树
     *
     * @param dataGroup dataGroup
     * @return tree
     */
    private List<HostGroupTreeVO> getAuthorizedHostGroupTree(List<DataGroupDTO> dataGroup) {
        List<HostGroupTreeVO> groupList = HostGroupConvert.MAPPER.toList(dataGroup);
        HostGroupTreeVO rootNode = HostGroupTreeVO.builder()
                .id(Const.ROOT_PARENT_ID)
                .sort(Const.DEFAULT_SORT)
                .build();
        TreeUtils.buildGroupTree(rootNode, groupList);
        return rootNode.getChildren();
    }

    /**
     * 获取主机分组树 主机节点映射
     *
     * @param allData               allData
     * @param dataGroup             dataGroup
     * @param dataGroupRel          dataGroupRel
     * @param authorizedGroupIdList authorizedGroupIdList
     * @return hostGroupId:hostIdList
     */
    private Map<String, Set<Long>> getAuthorizedHostGroupNodes(boolean allData,
                                                               List<DataGroupDTO> dataGroup,
                                                               Map<Long, Set<Long>> dataGroupRel,
                                                               List<Long> authorizedGroupIdList) {
        Map<String, Set<Long>> result = new HashMap<>();
        dataGroup.stream()
                .map(DataGroupDTO::getId)
                // 因为可能父菜单没有授权 这里需要判断分组权限
                .filter(id -> allData || authorizedGroupIdList.contains(id))
                .forEach(s -> result.put(String.valueOf(s), dataGroupRel.get(s)));
        return result;
    }

    /**
     * 查询已授权的所有主机
     *
     * @param allData               allData
     * @param dataGroup             dataGroup
     * @param dataGroupRel          dataGroupRel
     * @param authorizedGroupIdList authorizedGroupIdList
     * @return hosts
     */
    private List<HostVO> getAuthorizedHostList(boolean allData,
                                               List<DataGroupDTO> dataGroup,
                                               Map<Long, Set<Long>> dataGroupRel,
                                               List<Long> authorizedGroupIdList) {
        // 查询主机列表
        List<HostVO> hosts = hostService.getHostListByCache();
        // 全部数据直接返回
        if (allData) {
            return hosts;
        }
        Map<Long, HostVO> hostMap = hosts.stream()
                .collect(Collectors.toMap(HostVO::getId, Function.identity(), Functions.right()));
        // 仅设置已授权的数据
        return dataGroup.stream()
                .map(DataGroupDTO::getId)
                // 因为可能父菜单没有授权 这里需要判断分组权限
                .filter(authorizedGroupIdList::contains)
                .map(dataGroupRel::get)
                .filter(Lists::isNoneEmpty)
                .flatMap(Collection::stream)
                .distinct()
                .map(hostMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 设置授权主机的额外参数
     *
     * @param hosts    hosts
     * @param favorite favorite
     * @param aliasMap aliasMap
     */
    private void getAuthorizedHostExtra(List<HostVO> hosts,
                                        List<Long> favorite,
                                        Map<Long, String> aliasMap) {
        if (Lists.isEmpty(hosts)) {
            return;
        }
        // 设置收藏结果
        if (!Lists.isEmpty(favorite)) {
            hosts.forEach(s -> s.setFavorite(favorite.contains(s.getId())));
        }
        List<Long> hostIdList = hosts.stream()
                .map(HostVO::getId)
                .collect(Collectors.toList());
        // 查询 tag 信息
        List<List<TagDTO>> tags = tagRelApi.getRelTags(TagTypeEnum.HOST, hostIdList);
        for (int i = 0; i < hosts.size(); i++) {
            hosts.get(i).setTags(tags.get(i));
        }
        // 设置主机别名
        if (!Maps.isEmpty(aliasMap)) {
            hosts.forEach(s -> s.setAlias(aliasMap.get(s.getId())));
        }
    }

}
