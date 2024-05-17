package com.orion.visor.module.asset.service;

import com.orion.visor.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import com.orion.visor.module.asset.entity.vo.HostGroupTreeVO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRenameDTO;

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

}
