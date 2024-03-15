package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.ExecTemplateDO;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecTemplateUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecTemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 执行模板 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Mapper
public interface ExecTemplateConvert {

    ExecTemplateConvert MAPPER = Mappers.getMapper(ExecTemplateConvert.class);

    ExecTemplateDO to(ExecTemplateCreateRequest request);

    ExecTemplateDO to(ExecTemplateUpdateRequest request);

    ExecTemplateVO to(ExecTemplateDO domain);

}
