package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.HostConfigDO;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 主机配置 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostConfigConvert {

    HostConfigConvert MAPPER = Mappers.getMapper(HostConfigConvert.class);

    HostConfigVO to(HostConfigDO domain);

}
