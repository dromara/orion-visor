package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.module.infra.convert.FavoriteConvert;
import com.orion.ops.module.infra.dao.FavoriteDAO;
import com.orion.ops.module.infra.entity.domain.FavoriteDO;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteCreateRequest;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.ops.module.infra.entity.vo.FavoriteVO;
import com.orion.ops.module.infra.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public Long addFavorite(FavoriteCreateRequest request) {
        // 转换
        FavoriteDO record = FavoriteConvert.MAPPER.to(request);
        record.setId(null);
        // 插入
        int effect = favoriteDAO.insert(record);
        log.info("FavoriteService-addFavorite effect: {}, record: {}", effect, JSON.toJSONString(record));
        return record.getId();
    }

    @Override
    public List<FavoriteVO> getFavoriteList(FavoriteQueryRequest request) {
        // 条件
        LambdaQueryWrapper<FavoriteDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return favoriteDAO.of()
                .wrapper(wrapper)
                .list(FavoriteConvert.MAPPER::to);
    }

    @Override
    public List<Long> getFavoriteRelIdList(FavoriteQueryRequest request) {
        // 条件
        LambdaQueryWrapper<FavoriteDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return favoriteDAO.of()
                .wrapper(wrapper)
                .stream()
                .map(FavoriteDO::getRelId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Integer deleteFavorite(FavoriteQueryRequest request) {
        // 条件
        LambdaQueryWrapper<FavoriteDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        return favoriteDAO.delete(wrapper);
    }


    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<FavoriteDO> buildQueryWrapper(FavoriteQueryRequest request) {
        return favoriteDAO.wrapper()
                .eq(FavoriteDO::getId, request.getId())
                .eq(FavoriteDO::getUserId, request.getUserId())
                .eq(FavoriteDO::getRelId, request.getRelId())
                .eq(FavoriteDO::getType, request.getType())
                .in(FavoriteDO::getUserId, request.getUserIdList())
                .in(FavoriteDO::getRelId, request.getRelIdList());
    }

}
