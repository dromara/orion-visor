package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisLists;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.convert.FavoriteConvert;
import com.orion.ops.module.infra.dao.FavoriteDAO;
import com.orion.ops.module.infra.define.cache.FavoriteCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.FavoriteDO;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteOperatorRequest;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.ops.module.infra.entity.vo.FavoriteVO;
import com.orion.ops.module.infra.enums.FavoriteTypeEnum;
import com.orion.ops.module.infra.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Slf4j
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteDAO favoriteDAO;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Long addFavorite(FavoriteOperatorRequest request) {
        String type = Valid.valid(FavoriteTypeEnum::of, request.getType()).name();
        Long userId = SecurityUtils.getLoginUserId();
        // 转换
        FavoriteDO record = FavoriteConvert.MAPPER.to(request);
        record.setUserId(userId);
        // 插入
        int effect = favoriteDAO.insert(record);
        // 设置缓存
        String key = FavoriteCacheKeyDefine.FAVORITE.format(type, userId);
        RedisLists.push(key, request.getRelId(), String::valueOf);
        // 设置过期时间
        RedisLists.setExpire(key, FavoriteCacheKeyDefine.FAVORITE);
        return record.getId();
    }

    @Override
    public Integer cancelFavorite(FavoriteOperatorRequest request) {
        String type = Valid.valid(FavoriteTypeEnum::of, request.getType()).name();
        Long userId = SecurityUtils.getLoginUserId();
        Long relId = request.getRelId();
        // 删除库
        int effect = favoriteDAO.deleteFavorite(type, userId, relId);
        // 删除缓存
        String key = FavoriteCacheKeyDefine.FAVORITE.format(type, userId);
        redisTemplate.opsForList().remove(key, 1, relId.toString());
        return effect;
    }

    @Override
    public List<FavoriteVO> getFavoriteList(FavoriteQueryRequest request) {
        // 条件
        LambdaQueryWrapper<FavoriteDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return favoriteDAO.of(wrapper).list(FavoriteConvert.MAPPER::to);
    }

    @Override
    public List<Long> getFavoriteRelIdList(FavoriteQueryRequest request) {
        String type = request.getType();
        Long userId = request.getUserId();
        String cacheKey = FavoriteCacheKeyDefine.FAVORITE.format(type, userId);
        // 获取缓存
        List<Long> cacheRelIdList = RedisLists.range(cacheKey, Long::valueOf);
        if (cacheRelIdList.isEmpty()) {
            // 条件
            LambdaQueryWrapper<FavoriteDO> wrapper = this.buildQueryWrapper(request);
            // 查询数据库
            cacheRelIdList = favoriteDAO.of(wrapper)
                    .stream()
                    .map(FavoriteDO::getRelId)
                    .distinct()
                    .collect(Collectors.toList());
            // 添加默认值 防止穿透
            if (cacheRelIdList.isEmpty()) {
                cacheRelIdList.add(Const.NONE_ID);
            }
            // 设置缓存
            RedisLists.pushAll(cacheKey, cacheRelIdList, String::valueOf);
            // 设置过期时间
            RedisLists.setExpire(cacheKey, FavoriteCacheKeyDefine.FAVORITE);
        }
        // 删除防止穿透的 key
        cacheRelIdList.remove(Const.NONE_ID);
        return cacheRelIdList;
    }

    @Override
    public void deleteFavoriteByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        // 删除库
        favoriteDAO.deleteFavoriteByUserId(userId);
        // 删除缓存
        List<String> favoriteKeyList = Arrays.stream(FavoriteTypeEnum.values())
                .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
                .collect(Collectors.toList());
        redisTemplate.delete(favoriteKeyList);
    }

    @Override
    public void deleteFavoriteByUserIdList(List<Long> userIdList) {
        if (Lists.isEmpty(userIdList)) {
            return;
        }
        // 删除库
        favoriteDAO.deleteFavoriteByUserIdList(userIdList);
        // 删除缓存
        List<String> favoriteKeyList = new ArrayList<>();
        for (Long userId : userIdList) {
            Arrays.stream(FavoriteTypeEnum.values())
                    .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
                    .forEach(favoriteKeyList::add);
        }
        redisTemplate.delete(favoriteKeyList);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<FavoriteDO> buildQueryWrapper(FavoriteQueryRequest request) {
        return favoriteDAO.wrapper()
                .eq(FavoriteDO::getUserId, request.getUserId())
                .eq(FavoriteDO::getRelId, request.getRelId())
                .eq(FavoriteDO::getType, request.getType());
    }

}
