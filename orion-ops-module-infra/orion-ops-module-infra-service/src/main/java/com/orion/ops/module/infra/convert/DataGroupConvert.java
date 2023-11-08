package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DataGroupDO;
import com.orion.ops.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据分组 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupConvert {

    DataGroupConvert MAPPER = Mappers.getMapper(DataGroupConvert.class);

    DataGroupDO to(DataGroupCreateRequest request);

    DataGroupDO to(DataGroupUpdateRequest request);

    DataGroupCacheDTO toCache(DataGroupDO domain);

}
