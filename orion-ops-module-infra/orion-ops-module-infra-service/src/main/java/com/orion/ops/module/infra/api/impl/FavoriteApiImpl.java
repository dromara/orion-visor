package com.orion.ops.module.infra.api.impl;

import com.orion.ops.module.infra.api.FavoriteApi;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteCreateRequest;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.ops.module.infra.enums.FavoriteTypeEnum;
import com.orion.ops.module.infra.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class FavoriteApiImpl implements FavoriteApi {

    @Resource
    private FavoriteService favoriteService;

    @Override
    @Async("asyncExecutor")
    public void addFavorite(FavoriteTypeEnum type, Long userId, Long relId) {
        String typeName = type.name();
        FavoriteCreateRequest request = new FavoriteCreateRequest();
        request.setUserId(userId);
        request.setRelId(relId);
        request.setType(typeName);
        favoriteService.addFavorite(request);
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<Long>> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId) {
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setUserId(userId);
        request.setType(type.name());
        // 查询
        List<Long> relIdList = favoriteService.getFavoriteRelIdList(request);
        return CompletableFuture.completedFuture(relIdList);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteByUserId(Long userId) {
        favoriteService.deleteFavoriteByUserId(userId);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteByUserIdList(List<Long> userIdList) {
        favoriteService.deleteFavoriteByUserIdList(userIdList);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteByRelId(FavoriteTypeEnum type, Long relId) {
        favoriteService.deleteFavoriteByRelId(type.name(), relId);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteByRelIdList(FavoriteTypeEnum type, List<Long> relIdList) {
        favoriteService.deleteFavoriteByRelIdList(type.name(), relIdList);
    }

}
