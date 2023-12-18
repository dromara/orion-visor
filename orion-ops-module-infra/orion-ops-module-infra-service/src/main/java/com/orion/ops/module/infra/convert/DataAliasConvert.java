package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DataAliasDO;
import com.orion.ops.module.infra.entity.request.data.DataAliasUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据别名 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Mapper
public interface DataAliasConvert {

    DataAliasConvert MAPPER = Mappers.getMapper(DataAliasConvert.class);

    DataAliasDO to(DataAliasUpdateRequest request);

}
