package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.request.exec.ExecCommandExecRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 批量执行 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecConvert {

    ExecConvert MAPPER = Mappers.getMapper(ExecConvert.class);

    ExecCommandExecRequest to(ExecCommandRequest request);

}
