package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.SystemRoleDO;
import com.orion.visor.module.infra.entity.request.role.SystemRoleCreateRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleQueryRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleStatusRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleUpdateRequest;
import com.orion.visor.module.infra.entity.vo.SystemRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemRoleConvert {

    SystemRoleConvert MAPPER = Mappers.getMapper(SystemRoleConvert.class);

    SystemRoleDO to(SystemRoleCreateRequest request);

    SystemRoleDO to(SystemRoleStatusRequest request);

    SystemRoleDO to(SystemRoleUpdateRequest request);

    SystemRoleDO to(SystemRoleQueryRequest request);

    SystemRoleVO to(SystemRoleDO domain);

    List<SystemRoleVO> to(List<SystemRoleDO> list);

}
