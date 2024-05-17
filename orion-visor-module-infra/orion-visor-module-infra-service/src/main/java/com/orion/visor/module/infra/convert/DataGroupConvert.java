package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.DataGroupDO;
import com.orion.visor.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.visor.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.visor.module.infra.entity.request.data.DataGroupRenameRequest;
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

    DataGroupDO to(DataGroupRenameRequest request);

    DataGroupCacheDTO toCache(DataGroupDO domain);

}
