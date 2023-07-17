package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import com.orion.ops.module.infra.entity.request.menu.SystemRoleMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemRoleMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemRoleMenuUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemRoleMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
