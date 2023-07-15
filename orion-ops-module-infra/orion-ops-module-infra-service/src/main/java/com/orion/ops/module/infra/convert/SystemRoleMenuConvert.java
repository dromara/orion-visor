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
 * 角色菜单关联 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemRoleMenuConvert {
    
    SystemRoleMenuConvert MAPPER = Mappers.getMapper(SystemRoleMenuConvert.class);

    SystemRoleMenuDO to(SystemRoleMenuCreateRequest request);

    SystemRoleMenuDO to(SystemRoleMenuUpdateRequest request);

    SystemRoleMenuDO to(SystemRoleMenuQueryRequest request);

    SystemRoleMenuVO to(SystemRoleMenuDO domain);

    List<SystemRoleMenuVO> to(List<SystemRoleMenuDO> list);

}
