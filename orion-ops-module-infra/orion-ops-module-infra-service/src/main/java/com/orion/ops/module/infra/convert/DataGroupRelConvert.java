package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DataGroupRelDO;
import com.orion.ops.module.infra.entity.dto.DataGroupRelCacheDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据分组关联 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupRelConvert {

    DataGroupRelConvert MAPPER = Mappers.getMapper(DataGroupRelConvert.class);

    DataGroupRelDO to(DataGroupRelCreateRequest request);

    DataGroupRelCacheDTO toCache(DataGroupRelDO domain);

}
