package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.*;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;
import com.orion.ops.module.infra.entity.dto.data.*;
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

    DataGroupRelDTO to(DataGroupRelVO dto);

    DataGroupRelDO to(DataGroupRelDTO dto);

    DataGroupRelDTO to(DataGroupRelDO domain);

    DataGroupRelDO to(DataGroupRelQueryDTO domain);

    DataGroupRelDO to(DataGroupRelUpdateDTO update);

    DataGroupRelQueryRequest toRequest(DataGroupRelQueryDTO request);

    DataGroupRelCreateRequest toRequest(DataGroupRelCreateDTO request);

    DataGroupRelUpdateRequest toRequest(DataGroupRelUpdateDTO request);

    List<DataGroupRelDTO> toList(List<DataGroupRelDO> list);

}
