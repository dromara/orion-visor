package com.orion.visor.module.asset.service;

import com.orion.visor.module.asset.entity.request.path.PathBookmarkCreateRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkUpdateRequest;
import com.orion.visor.module.asset.entity.vo.PathBookmarkVO;
import com.orion.visor.module.asset.entity.vo.PathBookmarkWrapperVO;

import java.util.List;

/**
 * 路径标签 服务类
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
public interface PathBookmarkService {

    /**
     * 创建路径标签
     *
     * @param request request
     * @return id
     */
    Long createPathBookmark(PathBookmarkCreateRequest request);

    /**
     * 更新路径标签
     *
     * @param request request
     * @return effect
     */
    Integer updatePathBookmarkById(PathBookmarkUpdateRequest request);

    /**
     * 查询路径标签
     *
     * @return rows
     */
    PathBookmarkWrapperVO getPathBookmark();

    /**
     * 查询全部路径标签
     *
     * @return rows
     */
    List<PathBookmarkVO> getPathBookmarkList();

    /**
     * 删除路径标签
     *
     * @param id id
     * @return effect
     */
    Integer deletePathBookmarkById(Long id);

    /**
     * 设置分组为 null
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer setGroupNull(Long userId, Long groupId);

    /**
     * 通过 groupId 删除
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer deleteByGroupId(Long userId, Long groupId);

}
