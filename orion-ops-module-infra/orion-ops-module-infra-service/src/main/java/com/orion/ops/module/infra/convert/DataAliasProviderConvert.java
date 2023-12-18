package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DataAliasDO;
import com.orion.ops.module.infra.entity.dto.data.DataAliasDTO;
import com.orion.ops.module.infra.entity.dto.data.DataAliasQueryDTO;
import com.orion.ops.module.infra.entity.dto.data.DataAliasUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataAliasUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据别名 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Mapper
public interface DataAliasProviderConvert {

    DataAliasProviderConvert MAPPER = Mappers.getMapper(DataAliasProviderConvert.class);

    DataAliasDO to(DataAliasDTO dto);

    DataAliasDTO to(DataAliasDO domain);

    DataAliasDO to(DataAliasQueryDTO domain);

    DataAliasDO to(DataAliasUpdateDTO update);

    DataAliasUpdateRequest toRequest(DataAliasUpdateDTO request);

    List<DataAliasDTO> toList(List<DataAliasDO> list);

}
