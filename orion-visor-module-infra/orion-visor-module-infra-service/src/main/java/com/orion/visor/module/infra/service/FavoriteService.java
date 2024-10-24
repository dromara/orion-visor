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
package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.request.favorite.FavoriteOperatorRequest;
import com.orion.visor.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.visor.module.infra.entity.vo.FavoriteVO;

import java.util.List;

/**
 * 收藏 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
public interface FavoriteService {

    /**
     * 创建收藏
     *
     * @param request request
     * @return id
     */
    Long addFavorite(FavoriteOperatorRequest request);

    /**
     * 删除收藏
     *
     * @param request request
     * @return effect
     */
    Integer cancelFavorite(FavoriteOperatorRequest request);

    /**
     * 查询收藏列表
     *
     * @param request request
     * @return rows
     */
    List<FavoriteVO> getFavoriteList(FavoriteQueryRequest request);

    /**
     * 查询收藏 relId 列表
     *
     * @param request request
     * @return relIdList
     */
    List<Long> getFavoriteRelIdList(FavoriteQueryRequest request);

    /**
     * 通过 userId 删除收藏
     *
     * @param userId userId
     */
    void deleteFavoriteByUserId(Long userId);

    /**
     * 通过 userId 删除收藏
     *
     * @param userIdList userId
     */
    void deleteFavoriteByUserIdList(List<Long> userIdList);

}
