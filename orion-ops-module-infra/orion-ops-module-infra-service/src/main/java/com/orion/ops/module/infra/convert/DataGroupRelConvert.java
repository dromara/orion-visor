package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.*;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据分组关联 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupRelConvert {

    DataGroupRelConvert MAPPER = Mappers.getMapper(DataGroupRelConvert.class);

    DataGroupRelDO to(DataGroupRelCreateRequest request);

    DataGroupRelDO to(DataGroupRelUpdateRequest request);

    DataGroupRelDO to(DataGroupRelQueryRequest request);

    DataGroupRelVO to(DataGroupRelDO domain);

    List<DataGroupRelVO> to(List<DataGroupRelDO> list);

    DataGroupRelVO to(DataGroupRelCacheDTO cache);

    DataGroupRelCacheDTO toCache(DataGroupRelDO domain);

}
