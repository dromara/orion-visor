package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupMoveRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupRenameRequest;

import java.util.List;

/**
 * 数据分组 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupService {

    /**
     * 创建数据分组
     *
     * @param request request
     * @return id
     */
    Long createDataGroup(DataGroupCreateRequest request);

    /**
     * 重命名分组
     *
     * @param request request
     * @return effect
     */
    Integer renameDataGroup(DataGroupRenameRequest request);

    /**
     * 移动分组
     *
     * @param request request
     * @return effect
     */
    Integer moveDataGroup(DataGroupMoveRequest request);

    /**
     * 通过缓存查询数据分组 - 列表
     *
     * @param type type
     * @return rows
     */
    List<DataGroupCacheDTO> getDataGroupListByCache(String type);

    /**
     * 通过缓存查询数据分组 - 树结构
     *
     * @param type type
     * @return rows
     */
    List<DataGroupCacheDTO> getDataGroupTreeByCache(String type);

    /**
     * 删除数据分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupById(Long id);

}
