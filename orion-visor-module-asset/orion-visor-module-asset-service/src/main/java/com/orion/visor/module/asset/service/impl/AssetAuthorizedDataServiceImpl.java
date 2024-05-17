package com.orion.visor.module.asset.service.impl;

import com.orion.lang.function.Functions;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.collect.Sets;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.utils.TreeUtils;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.asset.convert.HostGroupConvert;
import com.orion.visor.module.asset.entity.request.asset.AssetAuthorizedDataQueryRequest;
import com.orion.visor.module.asset.entity.vo.*;
import com.orion.visor.module.asset.enums.HostConfigTypeEnum;
import com.orion.visor.module.asset.enums.HostConnectTypeEnum;
import com.orion.visor.module.asset.enums.HostExtraItemEnum;
import com.orion.visor.module.asset.handler.host.extra.model.HostLabelExtraModel;
import com.orion.visor.module.asset.service.*;
import com.orion.visor.module.infra.api.*;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.dto.tag.TagDTO;
import com.orion.visor.module.infra.enums.*;
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
    private HostService hostService;

    @Resource
    private HostConfigService hostConfigService;

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
    private DataExtraApi dataExtraApi;

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
    public List<Long> getUserAuthorizedHostIdWithEnabledConfig(Long userId, HostConfigTypeEnum type) {
        // 获取启用的主机
        List<Long> hostIdList = this.getUserAuthorizedHostId(userId);
        if (hostIdList.isEmpty()) {
            return hostIdList;
        }
        // 获取启用配置的主机
        return hostConfigService.getEnabledConfigHostId(type.name(), hostIdList);
    }

    @SneakyThrows
    @Override
    public AuthorizedHostWrapperVO getUserAuthorizedHost(Long userId, String type) {
        // 查询授权的数据
        List<Long> authorizedGroupIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_GROUP, userId);
        if (Lists.isEmpty(authorizedGroupIdList)) {
            // 无数据
            return AuthorizedHostWrapperVO.builder()
                    .groupTree(Lists.empty())
                    .treeNodes(Maps.empty())
                    .hostList(Lists.empty())
                    .latestHosts(Sets.empty())
                    .build();
        }
        AuthorizedHostWrapperVO wrapper = new AuthorizedHostWrapperVO();
        // 查询我的收藏
        Future<List<Long>> favoriteResult = favoriteApi.getFavoriteRelIdListAsync(FavoriteTypeEnum.HOST, userId);
        // 查询最近连接的主机
        Future<List<Long>> latestConnectHostIdList = hostConnectLogService.getLatestConnectHostIdAsync(HostConnectTypeEnum.of(type), userId);
        // 查询主机拓展信息
        Future<Map<Long, String>> labelExtraResult = dataExtraApi.getExtraItemValuesByCacheAsync(userId,
                DataExtraTypeEnum.HOST,
                HostExtraItemEnum.LABEL.getItem());
        // 查询分组
        List<DataGroupDTO> dataGroup = dataGroupApi.getDataGroupList(DataGroupTypeEnum.HOST);
        // 查询分组引用
        Map<Long, Set<Long>> dataGroupRel = dataGroupRelApi.getGroupRelList(DataGroupTypeEnum.HOST);
        // 过滤掉无分组权限以及未启用配置的主机
        this.filterEnabledAuthorizedHost(dataGroup, dataGroupRel, authorizedGroupIdList, type);
        // 设置主机分组树
        wrapper.setGroupTree(this.getAuthorizedHostGroupTree(dataGroup));
        // 设置主机列表
        wrapper.setHostList(this.getAuthorizedHostList(dataGroupRel));
        // 设置主机分组节点映射
        wrapper.setTreeNodes(Maps.map(dataGroupRel, String::valueOf, Function.identity()));
        // 设置主机拓展信息
        this.getAuthorizedHostExtra(wrapper.getHostList(),
                favoriteResult.get(),
                labelExtraResult.get());
        // 设置最近连接的主机
        wrapper.setLatestHosts(new LinkedHashSet<>(latestConnectHostIdList.get()));
        return wrapper;
    }

    @Override
    public List<HostKeyVO> getUserAuthorizedHostKey(Long userId) {
        //  查询授权的数据
        List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_KEY, userId);
        if (authorizedIdList.isEmpty()) {
            return Lists.empty();
        }
        // 查询数据
        return hostKeyService.getHostKeyList()
                .stream()
                .filter(s -> authorizedIdList.contains(s.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HostIdentityVO> getUserAuthorizedHostIdentity(Long userId) {
        // 查询授权的数据
        List<Long> authorizedIdList = dataPermissionApi.getUserAuthorizedRelIdList(DataPermissionTypeEnum.HOST_IDENTITY, userId);
        if (authorizedIdList.isEmpty()) {
            return Lists.empty();
        }
        // 查询数据
        return hostIdentityService.getHostIdentityList()
                .stream()
                .filter(s -> authorizedIdList.contains(s.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 过滤掉未授权的 dataGroupRel 和 dataGroupRel
     * 过滤掉未启用配置的 dataGroupRel
     *
     * @param dataGroup             dataGroup
     * @param dataGroupRel          dataGroupRel
     * @param authorizedGroupIdList authorizedGroupIdList
     * @param type                  type
     */
    private void filterEnabledAuthorizedHost(List<DataGroupDTO> dataGroup,
                                             Map<Long, Set<Long>> dataGroupRel,
                                             List<Long> authorizedGroupIdList,
                                             String type) {
        // 过滤未授权的分组
        List<DataGroupDTO> authorizedDataGroup = new ArrayList<>();
        TreeUtils.getAllNodes(dataGroup, authorizedGroupIdList, authorizedDataGroup);
        dataGroup.clear();
        dataGroup.addAll(new HashSet<>(authorizedDataGroup));
        // 移除未授权的分组引用
        dataGroupRel.keySet().removeIf(s -> !authorizedGroupIdList.contains(s));
        // 查询配置已启用的主机
        List<Long> hostIdList = dataGroupRel.values()
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        List<Long> enabledConfigHostId = hostConfigService.getEnabledConfigHostId(type, hostIdList);
        // 从分组引用中移除未启用的主机
        dataGroupRel.forEach((k, v) -> v.removeIf(s -> !enabledConfigHostId.contains(s)));
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
     * 查询已授权的主机列表
     *
     * @param dataGroupRel dataGroupRel
     * @return hosts
     */
    private List<HostVO> getAuthorizedHostList(Map<Long, Set<Long>> dataGroupRel) {
        // 查询主机列表
        Map<Long, HostVO> hostMap = hostService.getHostListByCache()
                .stream()
                .collect(Collectors.toMap(HostVO::getId, Function.identity(), Functions.right()));
        // 设置已授权的数据
        return dataGroupRel.values()
                .stream()
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
     * @param hosts      hosts
     * @param favorite   favorite
     * @param labelExtra labelExtra
     */
    private void getAuthorizedHostExtra(List<HostVO> hosts,
                                        List<Long> favorite,
                                        Map<Long, String> labelExtra) {
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
        // 这种主机标签信息
        for (HostVO host : hosts) {
            String extra = labelExtra.get(host.getId());
            if (extra == null) {
                continue;
            }
            HostLabelExtraModel label = HostExtraItemEnum.LABEL.parse(extra);
            if (label == null) {
                continue;
            }
            host.setAlias(label.getAlias());
            host.setColor(label.getColor());
        }
    }

}
