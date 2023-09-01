package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.FavoriteDO;
import org.apache.ibatis.annotations.Mapper;

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
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<FavoriteDO> queryCondition(FavoriteDO entity) {
        return this.wrapper()
                .eq(FavoriteDO::getId, entity.getId())
                .eq(FavoriteDO::getUserId, entity.getUserId())
                .eq(FavoriteDO::getRelId, entity.getRelId())
                .eq(FavoriteDO::getType, entity.getType());
    }

}
