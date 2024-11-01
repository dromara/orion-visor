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
package org.dromara.visor.module.infra.api.impl;

import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.infra.api.FavoriteApi;
import org.dromara.visor.module.infra.dao.FavoriteDAO;
import org.dromara.visor.module.infra.entity.request.favorite.FavoriteQueryRequest;
import org.dromara.visor.module.infra.enums.FavoriteTypeEnum;
import org.dromara.visor.module.infra.service.FavoriteService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 收藏 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Service
public class FavoriteApiImpl implements FavoriteApi {

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private FavoriteDAO favoriteDAO;

    @Override
    public List<Long> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId) {
        Valid.allNotNull(type, userId);
        // 查询
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setType(type.name());
        request.setUserId(userId);
        return favoriteService.getFavoriteRelIdList(request);
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<Long>> getFavoriteRelIdListAsync(FavoriteTypeEnum type, Long userId) {
        Valid.allNotNull(type, userId);
        // 查询
        return CompletableFuture.completedFuture(this.getFavoriteRelIdList(type, userId));
    }

    @Override
    public void deleteByRelId(FavoriteTypeEnum type, Long relId) {
        Valid.allNotNull(type, relId);
        favoriteDAO.deleteFavoriteByRelId(type.name(), relId);
    }

    @Override
    public void deleteByRelIdList(FavoriteTypeEnum type, List<Long> relIdList) {
        Valid.notNull(type);
        Valid.notEmpty(relIdList);
        favoriteDAO.deleteFavoriteByRelIdList(type.name(), relIdList);
    }

}
