package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.domain.PathBookmarkDO;
import com.orion.visor.module.asset.entity.dto.PathBookmarkCacheDTO;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkCreateRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkUpdateRequest;
import com.orion.visor.module.asset.entity.vo.PathBookmarkVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 路径标签 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Mapper
public interface PathBookmarkConvert {

    PathBookmarkConvert MAPPER = Mappers.getMapper(PathBookmarkConvert.class);

    PathBookmarkDO to(PathBookmarkCreateRequest request);

    PathBookmarkDO to(PathBookmarkUpdateRequest request);

    PathBookmarkVO to(PathBookmarkDO domain);

    List<PathBookmarkVO> to(List<PathBookmarkDO> list);

    PathBookmarkVO to(PathBookmarkCacheDTO cache);

    PathBookmarkCacheDTO toCache(PathBookmarkDO domain);

}
