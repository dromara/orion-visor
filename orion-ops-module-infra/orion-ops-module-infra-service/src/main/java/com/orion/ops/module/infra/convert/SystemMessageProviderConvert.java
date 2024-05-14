package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.dto.message.SystemMessageCreateDTO;
import com.orion.ops.module.infra.entity.request.message.SystemMessageCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 系统消息 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Mapper
public interface SystemMessageProviderConvert {

    SystemMessageProviderConvert MAPPER = Mappers.getMapper(SystemMessageProviderConvert.class);

    SystemMessageCreateRequest toRequest(SystemMessageCreateDTO request);

}
