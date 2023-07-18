package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuCacheDTO;
import com.orion.ops.module.infra.entity.request.menu.*;
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

    SystemMenuCreateValidMenuRequest toMenuValidate(SystemMenuCreateRequest request);

    SystemMenuCreateValidFunctionRequest toFunctionValidate(SystemMenuCreateRequest request);

    SystemMenuCreateRequest toCreateValidate(SystemMenuUpdateRequest request);

    SystemMenuDO to(SystemMenuCreateRequest request);

    SystemMenuDO to(SystemMenuUpdateRequest request);

    SystemMenuDO to(SystemMenuQueryRequest request);

    SystemMenuVO to(SystemMenuDO domain);

    SystemMenuVO to(SystemMenuCacheDTO cache);

    List<SystemMenuVO> to(List<SystemMenuDO> list);

    SystemMenuCacheDTO toCache(SystemMenuDO domain);

    List<SystemMenuCacheDTO> toCache(List<SystemMenuDO> list);

}
