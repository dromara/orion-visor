/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.service;

import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupCreateRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupDeleteRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupUpdateRequest;
import com.orion.visor.module.asset.entity.vo.PathBookmarkGroupVO;

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

    /**
     * 清理未使用的分组
     */
    void clearUnusedGroup();

}
