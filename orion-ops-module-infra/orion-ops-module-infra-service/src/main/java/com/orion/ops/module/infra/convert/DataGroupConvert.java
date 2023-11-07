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
 * 数据分组 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupConvert {

    DataGroupConvert MAPPER = Mappers.getMapper(DataGroupConvert.class);

    DataGroupDO to(DataGroupCreateRequest request);

    DataGroupDO to(DataGroupUpdateRequest request);

    DataGroupDO to(DataGroupQueryRequest request);

    DataGroupVO to(DataGroupDO domain);

    List<DataGroupVO> to(List<DataGroupDO> list);

    DataGroupVO to(DataGroupCacheDTO cache);

    DataGroupCacheDTO toCache(DataGroupDO domain);

}
