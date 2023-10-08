package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.PreferenceDO;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdateRequest;
import com.orion.ops.module.infra.entity.vo.PreferenceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 用户偏好 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Mapper
public interface PreferenceConvert {

    PreferenceConvert MAPPER = Mappers.getMapper(PreferenceConvert.class);

    @Mapping(target = "config", ignore = true)
    PreferenceDO to(PreferenceUpdateRequest request);

    @Mapping(target = "config", ignore = true)
    PreferenceVO to(PreferenceDO domain);

}
