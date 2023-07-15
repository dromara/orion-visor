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

    SystemRoleDO to(SystemRoleUpdateRequest request);

    SystemRoleDO to(SystemRoleQueryRequest request);

    SystemRoleVO to(SystemRoleDO domain);

    List<SystemRoleVO> to(List<SystemRoleDO> list);

}
