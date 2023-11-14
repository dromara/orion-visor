package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.dto.data.DataGroupRelCreateDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据分组关联 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupRelProviderConvert {

    DataGroupRelProviderConvert MAPPER = Mappers.getMapper(DataGroupRelProviderConvert.class);

    DataGroupRelCreateRequest toRequest(DataGroupRelCreateDTO request);

    List<DataGroupRelCreateRequest> toList(List<DataGroupRelCreateDTO> list);

}
