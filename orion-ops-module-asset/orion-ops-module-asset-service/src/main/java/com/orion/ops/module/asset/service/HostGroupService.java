package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.host.HostGroupGrantQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostGroupGrantRequest;
import com.orion.ops.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostGroupTreeVO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupMoveDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRenameDTO;

import java.util.List;
import java.util.Set;

/**
 * 主机分组服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 10:59
 */
public interface HostGroupService {

    /**
     * 创建主机分组
     *
     * @param request request
     * @return id
     */
    Long createHostGroup(DataGroupCreateDTO request);

    /**
     * 查询主机分组树
     *
     * @return tree
     */
    List<HostGroupTreeVO> queryHostGroupTree();

    /**
     * 更新主机分组名称
     *
     * @param request request
     * @return effect
     */
    Integer updateHostGroupName(DataGroupRenameDTO request);

    /**
     * 移动主机分组
     *
     * @param request request
     * @return effect
     */
    Integer moveHostGroup(DataGroupMoveDTO request);

    /**
     * 删除主机分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostGroup(Long id);

    /**
     * 查询主机分组内主机
     *
     * @param groupId groupId
     * @return hostId
     */
    Set<Long> queryHostGroupRel(Long groupId);

    /**
     * 修改主机分组内主机
     *
     * @param request request
     */
    void updateHostGroupRel(HostGroupRelUpdateRequest request);

    /**
     * 获取已授权的分组
     *
     * @param request request
     * @return grantGroupId
     */
    List<Long> getAuthorizedHostGroup(HostGroupGrantQueryRequest request);

    /**
     * 授权主机分组
     *
     * @param request request
     */
    void grantHostGroup(HostGroupGrantRequest request);

    /**
     * 查询用户已授权的主机分组和主机
     *
     * @param userId userId
     * @return group
     */
    List<HostGroupTreeVO> getUserAuthorizedHostGroup(Long userId);

}
