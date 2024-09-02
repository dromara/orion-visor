package com.orion.visor.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.annotation.Keep;
import com.orion.lang.utils.collect.Lists;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisLists;
import com.orion.visor.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.infra.convert.FavoriteConvert;
import com.orion.visor.module.infra.dao.FavoriteDAO;
import com.orion.visor.module.infra.define.cache.FavoriteCacheKeyDefine;
import com.orion.visor.module.infra.entity.domain.FavoriteDO;
import com.orion.visor.module.infra.entity.request.favorite.FavoriteOperatorRequest;
import com.orion.visor.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.visor.module.infra.entity.vo.FavoriteVO;
import com.orion.visor.module.infra.enums.FavoriteTypeEnum;
import com.orion.visor.module.infra.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Keep
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Long addFavorite(FavoriteOperatorRequest request) {
        String type = Valid.valid(FavoriteTypeEnum::of, request.getType()).name();
        Long userId = SecurityUtils.getLoginUserId();
        // 检查是否存在
        FavoriteDO check = favoriteDAO.of()
                .createWrapper()
                .eq(FavoriteDO::getUserId, userId)
                .eq(FavoriteDO::getType, request.getType())
                .eq(FavoriteDO::getRelId, request.getRelId())
                .then()
                .getOne();
        if (check != null) {
            return check.getId();
        }
        // 转换
        FavoriteDO record = FavoriteConvert.MAPPER.to(request);
        record.setUserId(userId);
        // 插入
        favoriteDAO.insert(record);
        // 删除缓存
        RedisLists.delete(FavoriteCacheKeyDefine.FAVORITE.format(type, userId));
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
        RedisLists.delete(FavoriteCacheKeyDefine.FAVORITE.format(type, userId));
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
        List<Long> list = RedisLists.range(cacheKey, Long::valueOf);
        if (list.isEmpty()) {
            // 条件
            LambdaQueryWrapper<FavoriteDO> wrapper = this.buildQueryWrapper(request);
            // 查询数据库
            list = favoriteDAO.of(wrapper)
                    .stream()
                    .map(FavoriteDO::getRelId)
                    .distinct()
                    .collect(Collectors.toList());
            // 设置屏障 防止穿透
            CacheBarriers.LIST.check(list);
            // 设置缓存
            RedisLists.pushAll(cacheKey, FavoriteCacheKeyDefine.FAVORITE, list, String::valueOf);
        }
        // 删除屏障
        CacheBarriers.LIST.remove(list);
        return list;
    }

    @Override
    public void deleteFavoriteByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        // 删除
        this.deleteFavoriteByUserIdList(Lists.singleton(userId));
    }

    @Override
    public void deleteFavoriteByUserIdList(List<Long> userIdList) {
        if (Lists.isEmpty(userIdList)) {
            return;
        }
        // 删除库
        favoriteDAO.deleteFavoriteByUserIdList(userIdList);
        // 缓存自动过期
        // List<String> favoriteKeyList = new ArrayList<>();
        // for (Long userId : userIdList) {
        //     Arrays.stream(FavoriteTypeEnum.values())
        //             .map(s -> FavoriteCacheKeyDefine.FAVORITE.format(s, userId))
        //             .forEach(favoriteKeyList::add);
        // }
        // redisTemplate.delete(favoriteKeyList);
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
