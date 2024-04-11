package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.ExecJobDO;
import com.orion.ops.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 计划任务 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobConvert {

    ExecJobConvert MAPPER = Mappers.getMapper(ExecJobConvert.class);

    ExecJobDO to(ExecJobCreateRequest request);

    ExecJobDO to(ExecJobUpdateRequest request);

    ExecJobDO to(ExecJobQueryRequest request);

    ExecJobVO to(ExecJobDO domain);

    List<ExecJobVO> to(List<ExecJobDO> list);

}
