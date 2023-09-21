package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.HostIdentityDO;
import com.orion.ops.module.asset.entity.dto.HostIdentityCacheDTO;
import com.orion.ops.module.asset.entity.request.host.HostIdentityCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostIdentityQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostIdentityUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostIdentityVO;
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

    HostIdentityVO to(HostIdentityCacheDTO cache);

    HostIdentityCacheDTO toCache(HostIdentityDO domain);

    List<HostIdentityVO> to(List<HostIdentityDO> list);

}
