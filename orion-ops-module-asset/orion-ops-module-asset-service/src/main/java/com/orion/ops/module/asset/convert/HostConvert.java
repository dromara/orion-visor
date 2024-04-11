package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.HostCacheDTO;
import com.orion.ops.module.asset.entity.request.host.HostCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostConvert {

    HostConvert MAPPER = Mappers.getMapper(HostConvert.class);

    HostDO to(HostCreateRequest request);

    HostDO to(HostUpdateRequest request);

    HostDO to(HostQueryRequest request);

    HostVO to(HostDO domain);

    HostVO to(HostCacheDTO cache);

    HostCacheDTO toCache(HostDO domain);

    List<HostVO> toList(List<HostDO> domain);

}
