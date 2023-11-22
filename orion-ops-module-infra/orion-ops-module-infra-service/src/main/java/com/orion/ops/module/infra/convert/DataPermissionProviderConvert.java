package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.*;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.define.operator.*;
import com.orion.ops.module.infra.entity.dto.data.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据权限 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Mapper
public interface DataPermissionProviderConvert {

    DataPermissionProviderConvert MAPPER = Mappers.getMapper(DataPermissionProviderConvert.class);

    DataPermissionDO to(DataPermissionDTO dto);

    DataPermissionDTO to(DataPermissionDO domain);

    DataPermissionDO to(DataPermissionQueryDTO domain);

    DataPermissionDO to(DataPermissionUpdateDTO update);

    DataPermissionCreateRequest toRequest(DataPermissionCreateDTO request);

    DataPermissionUpdateRequest toRequest(DataPermissionUpdateDTO request);

    List<DataPermissionDTO> toList(List<DataPermissionDO> list);

}
