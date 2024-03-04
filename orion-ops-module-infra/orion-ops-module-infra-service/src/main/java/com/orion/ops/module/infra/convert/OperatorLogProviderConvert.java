package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.OperatorLogDO;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 操作日志 对外对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Mapper
public interface OperatorLogProviderConvert {

    OperatorLogProviderConvert MAPPER = Mappers.getMapper(OperatorLogProviderConvert.class);

    OperatorLogDTO to(OperatorLogDO domain);

}
