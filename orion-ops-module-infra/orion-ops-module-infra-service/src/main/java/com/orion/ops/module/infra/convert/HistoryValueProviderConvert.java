package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.HistoryValueDO;
import com.orion.ops.module.infra.entity.dto.history.HistoryValueCreateDTO;
import com.orion.ops.module.infra.entity.dto.history.HistoryValueDTO;
import com.orion.ops.module.infra.entity.request.history.HistoryValueCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 历史归档 对外服务对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
@Mapper
public interface HistoryValueProviderConvert {

    HistoryValueProviderConvert MAPPER = Mappers.getMapper(HistoryValueProviderConvert.class);

    HistoryValueDTO to(HistoryValueDO domain);

    HistoryValueCreateRequest toRequest(HistoryValueCreateDTO request);

    List<HistoryValueDTO> toList(List<HistoryValueDO> list);

}
