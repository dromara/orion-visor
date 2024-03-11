package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 批量执行日志 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Mapper
public interface ExecLogConvert {

    ExecLogConvert MAPPER = Mappers.getMapper(ExecLogConvert.class);

    ExecLogDO to(ExecLogQueryRequest request);

    ExecLogVO to(ExecLogDO domain);

    List<ExecLogVO> to(List<ExecLogDO> list);

}
