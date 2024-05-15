package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.HistoryValueDO;
import com.orion.visor.module.infra.entity.request.history.HistoryValueCreateRequest;
import com.orion.visor.module.infra.entity.request.history.HistoryValueQueryRequest;
import com.orion.visor.module.infra.entity.vo.HistoryValueVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 历史归档 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface HistoryValueConvert {

    HistoryValueConvert MAPPER = Mappers.getMapper(HistoryValueConvert.class);

    HistoryValueDO to(HistoryValueCreateRequest request);

    HistoryValueDO to(HistoryValueQueryRequest request);

    HistoryValueVO to(HistoryValueDO domain);

    List<HistoryValueVO> to(List<HistoryValueDO> list);

}
