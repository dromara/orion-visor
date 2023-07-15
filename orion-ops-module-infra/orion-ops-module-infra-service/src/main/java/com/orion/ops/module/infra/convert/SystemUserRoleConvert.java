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
