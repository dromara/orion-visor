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
 * 用户角色关联 暴露服务转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemUserRoleProviderConvert {

    SystemUserRoleProviderConvert MAPPER = Mappers.getMapper(SystemUserRoleProviderConvert.class);

    SystemUserRoleDO to(SystemUserRoleDTO dto);

    SystemUserRoleDTO to(SystemUserRoleDO domain);

    List<SystemUserRoleDO> toDO(List<SystemUserRoleDTO> list);

    List<SystemUserRoleDTO> toDTO(List<SystemUserRoleDO> list);

}
