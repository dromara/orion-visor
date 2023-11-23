package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.dto.user.SystemUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户 对外对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Mapper
public interface SystemUserProviderConvert {

    SystemUserProviderConvert MAPPER = Mappers.getMapper(SystemUserProviderConvert.class);

    SystemUserDTO to(SystemUserDO domain);

}
