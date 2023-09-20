package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.*;
import com.orion.ops.module.asset.entity.vo.*;
import com.orion.ops.module.asset.entity.request.host.*;
import com.orion.ops.module.asset.entity.export.*;
import com.orion.ops.module.asset.convert.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机身份 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostIdentityConvert {

    HostIdentityConvert MAPPER = Mappers.getMapper(HostIdentityConvert.class);

    HostIdentityDO to(HostIdentityCreateRequest request);

    HostIdentityDO to(HostIdentityUpdateRequest request);

    HostIdentityDO to(HostIdentityQueryRequest request);

    HostIdentityVO to(HostIdentityDO domain);

    HostIdentityExport toExport(HostIdentityDO domain);

    List<HostIdentityVO> to(List<HostIdentityDO> list);

}
