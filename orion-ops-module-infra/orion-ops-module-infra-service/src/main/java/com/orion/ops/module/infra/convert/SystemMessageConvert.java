package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.SystemMessageDO;
import com.orion.ops.module.infra.entity.request.message.SystemMessageCreateRequest;
import com.orion.ops.module.infra.entity.request.message.SystemMessageQueryRequest;
import com.orion.ops.module.infra.entity.vo.SystemMessageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 系统消息 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Mapper
public interface SystemMessageConvert {

    SystemMessageConvert MAPPER = Mappers.getMapper(SystemMessageConvert.class);

    SystemMessageDO to(SystemMessageCreateRequest request);

    SystemMessageDO to(SystemMessageQueryRequest request);

    SystemMessageVO to(SystemMessageDO domain);

    List<SystemMessageVO> to(List<SystemMessageDO> list);

}
