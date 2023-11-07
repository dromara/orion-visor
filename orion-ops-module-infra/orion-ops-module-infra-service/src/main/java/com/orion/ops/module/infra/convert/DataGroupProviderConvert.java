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
 * 数据分组 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupProviderConvert {

    DataGroupProviderConvert MAPPER = Mappers.getMapper(DataGroupProviderConvert.class);

    DataGroupDTO to(DataGroupVO dto);

    DataGroupDO to(DataGroupDTO dto);

    DataGroupDTO to(DataGroupDO domain);

    DataGroupDO to(DataGroupQueryDTO domain);

    DataGroupDO to(DataGroupUpdateDTO update);

    DataGroupQueryRequest toRequest(DataGroupQueryDTO request);

    DataGroupCreateRequest toRequest(DataGroupCreateDTO request);

    DataGroupUpdateRequest toRequest(DataGroupUpdateDTO request);

    List<DataGroupDTO> toList(List<DataGroupDO> list);

}
