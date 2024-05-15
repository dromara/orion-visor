package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.DataExtraDO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraSetDTO;
import com.orion.visor.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.visor.module.infra.entity.request.data.DataExtraSetRequest;
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

    DataExtraSetRequest to(DataExtraSetDTO update);

    DataExtraDTO to(DataExtraDO domain);

}
