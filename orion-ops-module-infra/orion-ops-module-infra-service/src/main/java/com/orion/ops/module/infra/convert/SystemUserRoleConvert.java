package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemUserRoleDO;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleCreateRequest;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleQueryRequest;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemUserRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户角色关联 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemUserRoleConvert {

    SystemUserRoleConvert MAPPER = Mappers.getMapper(SystemUserRoleConvert.class);

    SystemUserRoleDO to(SystemUserRoleCreateRequest request);

    SystemUserRoleDO to(SystemUserRoleUpdateRequest request);

    SystemUserRoleDO to(SystemUserRoleQueryRequest request);

    SystemUserRoleVO to(SystemUserRoleDO domain);

    List<SystemUserRoleVO> to(List<SystemUserRoleDO> list);

}
