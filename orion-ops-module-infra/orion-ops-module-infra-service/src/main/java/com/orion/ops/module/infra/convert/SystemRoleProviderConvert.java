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
 * 角色 暴露服务转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemRoleProviderConvert {

    SystemRoleProviderConvert MAPPER = Mappers.getMapper(SystemRoleProviderConvert.class);

    SystemRoleDO to(SystemRoleDTO dto);

    SystemRoleDTO to(SystemRoleDO domain);

    List<SystemRoleDO> toDO(List<SystemRoleDTO> list);

    List<SystemRoleDTO> toDTO(List<SystemRoleDO> list);

}
