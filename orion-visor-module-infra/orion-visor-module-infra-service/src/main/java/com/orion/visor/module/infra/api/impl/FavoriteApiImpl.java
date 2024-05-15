package com.orion.visor.module.infra.api.impl;

import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.FavoriteApi;
import com.orion.visor.module.infra.dao.FavoriteDAO;
import com.orion.visor.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.visor.module.infra.enums.FavoriteTypeEnum;
import com.orion.visor.module.infra.service.FavoriteService;
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
