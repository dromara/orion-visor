package com.orion.ops.module.infra.api.impl;

import com.orion.lang.utils.Strings;
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
        String cache = redisTemplate.opsForValue().get(key);
        List<Long> relIdList;
        if (Strings.isBlank(cache)) {
            relIdList = Lists.newList();
        } else {
            relIdList = Arrays.stream(cache.split(Const.COMMA))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
        // 插入缓存
        relIdList.add(relId);
        RedisUtils.set(key, FavoriteCacheKeyDefine.FAVORITE, Lists.join(relIdList));
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<Long>> getFavoriteRelIdList(FavoriteTypeEnum type, Long userId) {
        // 获取缓存
        String typeName = type.name();
        String key = FavoriteCacheKeyDefine.FAVORITE.format(typeName, userId);
        String cache = redisTemplate.opsForValue().get(key);
        List<Long> relIdList;
        if (cache != null) {
            // 不为 null 则获取缓存
            relIdList = Arrays.stream(cache.split(Const.COMMA))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } else {
            // 为 null 从数据库获取
            FavoriteQueryRequest request = new FavoriteQueryRequest();
            request.setUserId(userId);
            request.setType(typeName);
            relIdList = favoriteService.getFavoriteRelIdList(request);
            // 设置缓存
            redisTemplate.opsForValue().set(key, Lists.join(relIdList));
        }
        return CompletableFuture.completedFuture(relIdList);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByUserId(Long userId) {
        List<String> favoriteKeyList = Arrays.stream(FavoriteTypeEnum.values())
                .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
                .collect(Collectors.toList());
        redisTemplate.delete(favoriteKeyList);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByUserIdList(List<Long> userIdList) {
        List<String> favoriteKeyList = new ArrayList<>();
        for (Long userId : userIdList) {
            Arrays.stream(FavoriteTypeEnum.values())
                    .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
                    .forEach(favoriteKeyList::add);
        }
        redisTemplate.delete(favoriteKeyList);
    }

    @Override
    @Async("asyncExecutor")
    public void deleteFavoriteByRelId(Long relId) {
        // 只删除数据库 redis 等自动失效
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setRelId(relId);
        favoriteService.deleteFavorite(request);
    }

    @Override
    public void deleteFavoriteByRelIdList(List<Long> relIdList) {
        // 只删除数据库 redis 等自动失效
        FavoriteQueryRequest request = new FavoriteQueryRequest();
        request.setRelIdList(relIdList);
        favoriteService.deleteFavorite(request);
    }

}
