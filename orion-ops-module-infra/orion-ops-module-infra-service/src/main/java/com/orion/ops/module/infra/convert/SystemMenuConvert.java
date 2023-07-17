package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuCacheDTO;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 菜单 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Mapper
public interface SystemMenuConvert {

    SystemMenuConvert MAPPER = Mappers.getMapper(SystemMenuConvert.class);

    SystemMenuDO to(SystemMenuCreateRequest request);

    SystemMenuDO to(SystemMenuUpdateRequest request);

    SystemMenuDO to(SystemMenuQueryRequest request);

    SystemMenuVO to(SystemMenuDO domain);

    List<SystemMenuVO> to(List<SystemMenuDO> list);

    List<SystemMenuCacheDTO> toCache(List<SystemMenuDO> list);

}
