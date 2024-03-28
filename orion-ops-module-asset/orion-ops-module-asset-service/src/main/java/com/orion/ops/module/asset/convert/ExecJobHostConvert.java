package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.ExecJobHostDO;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobHostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 计划执行任务主机 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobHostConvert {

    ExecJobHostConvert MAPPER = Mappers.getMapper(ExecJobHostConvert.class);

    ExecJobHostDO to(ExecJobHostCreateRequest request);

    ExecJobHostDO to(ExecJobHostUpdateRequest request);

    ExecJobHostDO to(ExecJobHostQueryRequest request);

    ExecJobHostVO to(ExecJobHostDO domain);

    List<ExecJobHostVO> to(List<ExecJobHostDO> list);

}
