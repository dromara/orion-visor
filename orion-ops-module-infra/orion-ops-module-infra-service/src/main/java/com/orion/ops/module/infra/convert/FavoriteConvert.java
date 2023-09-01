package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.FavoriteDO;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteCreateRequest;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteQueryRequest;
import com.orion.ops.module.infra.entity.vo.FavoriteVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收藏 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Mapper
public interface FavoriteConvert {

    FavoriteConvert MAPPER = Mappers.getMapper(FavoriteConvert.class);

    FavoriteDO to(FavoriteCreateRequest request);

    FavoriteDO to(FavoriteQueryRequest request);

    FavoriteVO to(FavoriteDO domain);

}
