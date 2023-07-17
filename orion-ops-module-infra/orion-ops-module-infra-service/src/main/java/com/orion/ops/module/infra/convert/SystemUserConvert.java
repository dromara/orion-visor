package com.orion.ops.module.infra.convert;

import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.request.user.SystemUserCreateRequest;
import com.orion.ops.module.infra.entity.request.user.SystemUserQueryRequest;
import com.orion.ops.module.infra.entity.request.user.SystemUserUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Mapper
public interface SystemUserConvert {

    SystemUserConvert MAPPER = Mappers.getMapper(SystemUserConvert.class);

    SystemUserDO to(SystemUserCreateRequest request);

    SystemUserDO to(SystemUserUpdateRequest request);

    SystemUserDO to(SystemUserQueryRequest request);

    SystemUserVO to(SystemUserDO domain);

    List<SystemUserVO> to(List<SystemUserDO> list);

    LoginUser toLoginUser(SystemUserDO domain);


}
