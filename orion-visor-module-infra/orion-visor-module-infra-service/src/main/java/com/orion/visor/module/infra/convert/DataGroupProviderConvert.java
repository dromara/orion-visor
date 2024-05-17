package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.DataGroupDO;
import com.orion.visor.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import com.orion.visor.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.visor.module.infra.entity.request.data.DataGroupMoveRequest;
import com.orion.visor.module.infra.entity.request.data.DataGroupRenameRequest;
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

    DataGroupDO to(DataGroupDTO dto);

    DataGroupDTO to(DataGroupDO domain);

    DataGroupDO to(DataGroupRenameDTO update);

    DataGroupCreateRequest toRequest(DataGroupCreateDTO request);

    DataGroupRenameRequest toRequest(DataGroupRenameDTO request);

    DataGroupMoveRequest toRequest(DataGroupMoveDTO request);

    List<DataGroupDTO> toList(List<DataGroupCacheDTO> list);

}
