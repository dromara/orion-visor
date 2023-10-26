package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.DictValueDO;
import com.orion.ops.module.infra.entity.request.dict.DictValueCreateRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueQueryRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueUpdateRequest;
import com.orion.ops.module.infra.entity.vo.DictValueVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 字典配置值 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface DictValueConvert {

    DictValueConvert MAPPER = Mappers.getMapper(DictValueConvert.class);

    DictValueDO to(DictValueCreateRequest request);

    DictValueDO to(DictValueUpdateRequest request);

    DictValueDO to(DictValueQueryRequest request);

    DictValueVO to(DictValueDO domain);

    List<DictValueVO> to(List<DictValueDO> list);

}
