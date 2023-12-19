package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DataExtraDO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.ops.module.infra.entity.request.data.DataExtraUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据拓展信息 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Mapper
public interface DataExtraProviderConvert {

    DataExtraProviderConvert MAPPER = Mappers.getMapper(DataExtraProviderConvert.class);

    DataExtraQueryRequest to(DataExtraQueryDTO query);

    DataExtraUpdateRequest to(DataExtraUpdateDTO update);

    DataExtraDTO to(DataExtraDO domain);

}
