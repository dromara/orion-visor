package com.orion.visor.module.infra.convert;

import com.orion.visor.framework.common.security.LoginUser;
import com.orion.visor.module.infra.entity.domain.SystemUserDO;
import com.orion.visor.module.infra.entity.dto.UserInfoDTO;
import com.orion.visor.module.infra.entity.request.user.*;
import com.orion.visor.module.infra.entity.vo.SystemUserVO;
import com.orion.visor.module.infra.entity.vo.UserCollectInfoVO;
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

    SystemUserDO to(SystemUserUpdateStatusRequest request);

    SystemUserVO to(SystemUserDO domain);

    SystemUserVO to(UserInfoDTO user);

    List<SystemUserVO> to(List<SystemUserDO> list);

    LoginUser toLoginUser(UserLoginRequest request);

    LoginUser toLoginUser(SystemUserDO domain);

    UserInfoDTO toUserInfo(SystemUserDO domain);

    UserCollectInfoVO toCollectInfo(LoginUser user);

}
