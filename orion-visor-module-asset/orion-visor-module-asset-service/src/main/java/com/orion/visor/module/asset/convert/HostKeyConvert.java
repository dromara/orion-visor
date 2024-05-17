package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.domain.HostKeyDO;
import com.orion.visor.module.asset.entity.dto.HostKeyCacheDTO;
import com.orion.visor.module.asset.entity.request.host.HostKeyCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostKeyQueryRequest;
import com.orion.visor.module.asset.entity.request.host.HostKeyUpdateRequest;
import com.orion.visor.module.asset.entity.vo.HostKeyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机密钥 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostKeyConvert {

    HostKeyConvert MAPPER = Mappers.getMapper(HostKeyConvert.class);

    HostKeyDO to(HostKeyCreateRequest request);

    HostKeyDO to(HostKeyUpdateRequest request);

    HostKeyDO to(HostKeyQueryRequest request);

    HostKeyVO to(HostKeyDO domain);

    HostKeyVO to(HostKeyCacheDTO cache);

    HostKeyCacheDTO toCache(HostKeyDO domain);

    List<HostKeyVO> to(List<HostKeyDO> list);

}
