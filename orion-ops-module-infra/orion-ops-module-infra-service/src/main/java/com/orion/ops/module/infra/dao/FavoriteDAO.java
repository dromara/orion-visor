package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.FavoriteDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 收藏 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Mapper
public interface FavoriteDAO extends IMapper<FavoriteDO> {

    /**
     * 通过 relId 删除收藏
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return effect
     */
    default int deleteFavorite(String type, Long userId, Long relId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getType, type)
                .eq(FavoriteDO::getUserId, userId)
                .eq(FavoriteDO::getRelId, relId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除收藏
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    default int deleteFavoriteByRelId(String type, Long relId) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getType, type)
                .eq(FavoriteDO::getRelId, relId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除收藏
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    default int deleteFavoriteByRelIdList(String type, List<Long> relIdList) {
        LambdaQueryWrapper<FavoriteDO> wrapper = this.lambda()
                .eq(FavoriteDO::getType, type)
                .in(FavoriteDO::getRelId, relIdList);
        return this.delete(wrapper);
    }

}
