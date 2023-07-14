package com.orion.ops.module.infra.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.orion.ops.module.infra.entity.domain.*;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.entity.request.*;
import com.orion.ops.module.infra.convert.*;
import java.util.List;

/**
 * 用户 暴露服务转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Mapper
@SuppressWarnings("ALL")
public interface SystemUserProviderConvert {

    SystemUserProviderConvert MAPPER = Mappers.getMapper(SystemUserProviderConvert.class);

    SystemUserDO to(SystemUserDTO dto);

    SystemUserDTO to(SystemUserDO dto);

    List<SystemUserDO> toDO(List<SystemUserDTO> list);

    List<SystemUserDTO> toDTO(List<SystemUserDO> list);

}
