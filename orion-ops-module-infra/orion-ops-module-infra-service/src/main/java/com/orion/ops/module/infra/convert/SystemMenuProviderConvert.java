package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 菜单 暴露服务转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Mapper
public interface SystemMenuProviderConvert {

    SystemMenuProviderConvert MAPPER = Mappers.getMapper(SystemMenuProviderConvert.class);

    SystemMenuDO to(SystemMenuDTO dto);

    SystemMenuDTO to(SystemMenuDO domain);

    List<SystemMenuDO> toDO(List<SystemMenuDTO> list);

    List<SystemMenuDTO> toDTO(List<SystemMenuDO> list);

}
