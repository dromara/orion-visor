package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.HostConnectLogDO;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostConnectLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 主机连接日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface HostConnectLogConvert {

    HostConnectLogConvert MAPPER = Mappers.getMapper(HostConnectLogConvert.class);

    HostConnectLogDO to(HostConnectLogCreateRequest request);

    HostConnectLogDO to(HostConnectLogQueryRequest request);

    HostConnectLogVO to(HostConnectLogDO domain);

    HostConnectLogCreateRequest to(HostTerminalConnectDTO dto);

    List<HostConnectLogVO> to(List<HostConnectLogDO> list);

}
