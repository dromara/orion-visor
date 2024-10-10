package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.SystemSettingDO;
import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 系统设置 内部对象转换器
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Mapper
public interface SystemSettingConvert {

    SystemSettingConvert MAPPER = Mappers.getMapper(SystemSettingConvert.class);

    @Mapping(target = "value", ignore = true)
    SystemSettingDO to(SystemSettingUpdateRequest request);

}
