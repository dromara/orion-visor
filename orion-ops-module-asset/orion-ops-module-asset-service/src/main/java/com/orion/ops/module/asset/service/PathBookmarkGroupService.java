package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.path.PathBookmarkGroupCreateRequest;
import com.orion.ops.module.asset.entity.request.path.PathBookmarkGroupDeleteRequest;
import com.orion.ops.module.asset.entity.request.path.PathBookmarkGroupUpdateRequest;
import com.orion.ops.module.asset.entity.vo.PathBookmarkGroupVO;

import java.util.List;

/**
 * 路径标签分组 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
public interface PathBookmarkGroupService {

    /**
     * 创建路径标签分组
     *
     * @param request request
     * @return id
     */
    Long createPathBookmarkGroup(PathBookmarkGroupCreateRequest request);

    /**
     * 更新路径标签分组
     *
     * @param request request
     * @return effect
     */
    Integer updatePathBookmarkGroupById(PathBookmarkGroupUpdateRequest request);

    /**
     * 查询全部路径标签分组
     *
     * @return rows
     */
    List<PathBookmarkGroupVO> getPathBookmarkGroupList();

    /**
     * 删除路径标签分组
     *
     * @param request request
     * @return effect
     */
    Integer deletePathBookmarkGroup(PathBookmarkGroupDeleteRequest request);

}
