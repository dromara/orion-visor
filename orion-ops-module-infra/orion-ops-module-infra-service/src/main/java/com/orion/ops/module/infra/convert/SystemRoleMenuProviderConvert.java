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
 * 角色菜单关联 暴露服务转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemRoleMenuProviderConvert {

    SystemRoleMenuProviderConvert MAPPER = Mappers.getMapper(SystemRoleMenuProviderConvert.class);

    SystemRoleMenuDO to(SystemRoleMenuDTO dto);

    SystemRoleMenuDTO to(SystemRoleMenuDO domain);

    List<SystemRoleMenuDO> toDO(List<SystemRoleMenuDTO> list);

    List<SystemRoleMenuDTO> toDTO(List<SystemRoleMenuDO> list);

}
