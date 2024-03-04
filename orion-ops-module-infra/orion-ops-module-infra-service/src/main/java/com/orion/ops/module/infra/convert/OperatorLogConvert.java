package com.orion.ops.module.infra.convert;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.module.infra.entity.domain.OperatorLogDO;
import com.orion.ops.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.ops.module.infra.entity.vo.LoginHistoryVO;
import com.orion.ops.module.infra.entity.vo.OperatorLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 操作日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Mapper
public interface OperatorLogConvert {

    OperatorLogConvert MAPPER = Mappers.getMapper(OperatorLogConvert.class);

    OperatorLogDO to(OperatorLogModel model);

    OperatorLogDO to(OperatorLogQueryRequest request);

    @Mapping(target = "extra", ignore = true)
    OperatorLogVO to(OperatorLogDO domain);

    LoginHistoryVO toLoginHistory(OperatorLogDO domain);

}
