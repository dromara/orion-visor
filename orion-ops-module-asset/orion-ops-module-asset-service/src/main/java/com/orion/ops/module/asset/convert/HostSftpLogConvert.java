package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.vo.HostSftpLogVO;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * SFTP 操作日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface HostSftpLogConvert {

    HostSftpLogConvert MAPPER = Mappers.getMapper(HostSftpLogConvert.class);

    @Mapping(target = "extra", ignore = true)
    HostSftpLogVO to(OperatorLogDTO request);

}
