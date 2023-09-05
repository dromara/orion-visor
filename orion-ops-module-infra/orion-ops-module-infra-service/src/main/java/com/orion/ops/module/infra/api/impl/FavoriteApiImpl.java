package com.orion.ops.module.infra.api.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.module.infra.api.FavoriteApi;
import com.orion.ops.module.infra.define.FavoriteCacheKeyDefine;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteCreateRequest;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.ops.module.infra.enums.FavoriteTypeEnum;
import com.orion.ops.module.infra.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Async("asyncExecutor")
    public void addFavorite(FavoriteTypeEnum type, Long userId, Long relId) {
        // 插入数据库
        String typeName = type.name();
        FavoriteCreateRequest request = new FavoriteCreateRequest();
        request.setUserId(userId);
        request.setRelId(relId);
        request.setType(typeName);
        favoriteService.addFavorite(request);
        // 获取缓存
        String key = FavoriteCacheKeyDefine.FAVORITE.format(typeName, userId);
        RedisUtils.listPushAll(key, Lists.singleton(relId), String::valueOf);
        // 设置过期时间
        RedisUtils.setExpire(key, FavoriteCacheKeyDefine.FAVORITE);
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<Long>> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId) {
        String typeName = type.name();
        String key = FavoriteCacheKeyDefine.FAVORITE.format(typeName, userId);
        // 获取缓存
        List<Long> cacheRelIdList = RedisUtils.listRange(key, Long::valueOf);
        if (cacheRelIdList.isEmpty()) {
            // 查询数据库
            FavoriteQueryRequest request = new FavoriteQueryRequest();
            request.setUserId(userId);
            request.setType(typeName);
            cacheRelIdList = favoriteService.getFavoriteRelIdList(request);
            // 设置 -1 到缓存防止穿透
            if (cacheRelIdList.isEmpty()) {
                cacheRelIdList.add(Const.L_N_1);
            }
            // 设置缓存
            RedisUtils.listPushAll(key, cacheRelIdList, String::valueOf);
            // 设置过期时间
            RedisUtils.setExpire(key, FavoriteCacheKeyDefine.FAVORITE);
        }
        // 尝试删除防止穿透的 key
        cacheRelIdList.remove(Const.L_N_1);
        return CompletableFuture.completedFuture(cacheRelIdList);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        // 删除缓存
        List<String> favoriteKeyList = Arrays.stream(FavoriteTypeEnum.values())
                .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
                .collect(Collectors.toList());
        redisTemplate.delete(favoriteKeyList);
        // 删除库
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setUserId(userId);
        favoriteService.deleteFavorite(request);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByUserIdList(List<Long> userIdList) {
        if (Lists.isEmpty(userIdList)) {
            return;
        }
        // 删除缓存
        List<String> favoriteKeyList = new ArrayList<>();
        for (Long userId : userIdList) {
            Arrays.stream(FavoriteTypeEnum.values())
                    .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
                    .forEach(favoriteKeyList::add);
        }
        redisTemplate.delete(favoriteKeyList);
        // 删除库
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setUserIdList(userIdList);
        favoriteService.deleteFavorite(request);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByRelId(Long relId) {
        if (relId == null) {
            return;
        }
        // 只删除数据库 redis 等自动失效
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setRelId(relId);
        favoriteService.deleteFavorite(request);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByRelIdList(List<Long> relIdList) {
        if (Lists.isEmpty(relIdList)) {
            return;
        }
        // 只删除数据库 redis 等自动失效
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setRelIdList(relIdList);
        favoriteService.deleteFavorite(request);
    }

}
