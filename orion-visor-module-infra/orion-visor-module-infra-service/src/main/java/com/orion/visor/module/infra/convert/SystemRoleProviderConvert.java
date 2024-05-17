package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.SystemRoleDO;
import com.orion.visor.module.infra.entity.dto.role.SystemRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 角色 对外对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemRoleProviderConvert {

    SystemRoleProviderConvert MAPPER = Mappers.getMapper(SystemRoleProviderConvert.class);

    SystemRoleDTO to(SystemRoleDO domain);

}
