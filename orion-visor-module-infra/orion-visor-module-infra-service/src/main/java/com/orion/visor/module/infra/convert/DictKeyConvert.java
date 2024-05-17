package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.DictKeyDO;
import com.orion.visor.module.infra.entity.dto.DictKeyCacheDTO;
import com.orion.visor.module.infra.entity.request.dict.DictKeyCreateRequest;
import com.orion.visor.module.infra.entity.request.dict.DictKeyUpdateRequest;
import com.orion.visor.module.infra.entity.vo.DictKeyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 字典配置项 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface DictKeyConvert {

    DictKeyConvert MAPPER = Mappers.getMapper(DictKeyConvert.class);

    DictKeyDO to(DictKeyCreateRequest request);

    DictKeyDO to(DictKeyUpdateRequest request);

    DictKeyVO to(DictKeyDO domain);

    List<DictKeyVO> to(List<DictKeyDO> list);

    DictKeyVO to(DictKeyCacheDTO cache);

    DictKeyCacheDTO toCache(DictKeyDO domain);

}
