package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupCreateRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupUpdateRequest;
import com.orion.visor.module.asset.entity.vo.PathBookmarkGroupVO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 路径标签分组 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Mapper
public interface PathBookmarkGroupConvert {

    PathBookmarkGroupConvert MAPPER = Mappers.getMapper(PathBookmarkGroupConvert.class);

    DataGroupCreateDTO to(PathBookmarkGroupCreateRequest request);

    DataGroupRenameDTO to(PathBookmarkGroupUpdateRequest request);

    PathBookmarkGroupVO to(DataGroupDTO domain);

}
