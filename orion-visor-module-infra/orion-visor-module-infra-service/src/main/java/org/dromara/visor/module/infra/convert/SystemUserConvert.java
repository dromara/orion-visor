/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.convert;

import org.dromara.visor.framework.common.security.LoginUser;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.dto.UserInfoDTO;
import org.dromara.visor.module.infra.entity.request.user.*;
import org.dromara.visor.module.infra.entity.vo.SystemUserBaseVO;
import org.dromara.visor.module.infra.entity.vo.SystemUserVO;
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

    SystemUserBaseVO toBase(SystemUserDO user);

}
