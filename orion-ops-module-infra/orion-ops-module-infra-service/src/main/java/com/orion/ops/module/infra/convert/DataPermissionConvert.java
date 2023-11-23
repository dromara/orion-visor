package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DataPermissionDO;
import com.orion.ops.module.infra.entity.request.data.DataPermissionUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据权限 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Mapper
public interface DataPermissionConvert {

    DataPermissionConvert MAPPER = Mappers.getMapper(DataPermissionConvert.class);

    DataPermissionDO to(DataPermissionUpdateRequest request);

}
