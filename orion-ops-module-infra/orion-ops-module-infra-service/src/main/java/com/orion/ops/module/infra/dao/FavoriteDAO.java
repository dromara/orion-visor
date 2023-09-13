package com.orion.ops.module.infra.dao;

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

}
