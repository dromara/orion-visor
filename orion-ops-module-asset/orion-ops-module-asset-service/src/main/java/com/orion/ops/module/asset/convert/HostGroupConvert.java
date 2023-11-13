package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.vo.HostGroupTreeVO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机配置 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostGroupConvert {

    HostGroupConvert MAPPER = Mappers.getMapper(HostGroupConvert.class);

    List<HostGroupTreeVO> toList(List<DataGroupDTO> list);

}
