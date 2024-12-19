/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.asset.service;

import org.dromara.visor.module.asset.entity.request.path.PathBookmarkCreateRequest;
import org.dromara.visor.module.asset.entity.request.path.PathBookmarkUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.PathBookmarkVO;
import org.dromara.visor.module.asset.entity.vo.PathBookmarkWrapperVO;

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
     * 设置分组为 null
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer setGroupNull(Long userId, Long groupId);

    /**
     * 删除路径标签
     *
     * @param id id
     * @return effect
     */
    Integer deletePathBookmarkById(Long id);

    /**
     * 通过 groupId 删除
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer deleteByGroupId(Long userId, Long groupId);

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    Integer deleteByUserIdList(List<Long> userIdList);

}
