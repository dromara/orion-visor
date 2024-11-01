/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.module.infra.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.request.user.*;
import org.dromara.visor.module.infra.entity.vo.SystemUserVO;

import java.util.List;

/**
 * 用户 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
public interface SystemUserService {

    /**
     * 创建用户
     *
     * @param request request
     * @return id
     */
    Long createSystemUser(SystemUserCreateRequest request);

    /**
     * 通过 id 更新用户
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemUserById(SystemUserUpdateRequest request);

    /**
     * 修改用户状态
     *
     * @param request request
     * @return effect
     */
    Integer updateUserStatus(SystemUserUpdateStatusRequest request);

    /**
     * 通过 id 查询用户
     *
     * @param id id
     * @return row
     */
    SystemUserVO getSystemUserById(Long id);

    /**
     * 查询所有用户
     *
     * @return rows
     */
    List<SystemUserVO> getSystemUserList();

    /**
     * 分页查询用户
     *
     * @param request request
     * @return rows
     */
    DataGrid<SystemUserVO> getSystemUserPage(SystemUserQueryRequest request);

    /**
     * 查询系统用户数量
     *
     * @param request request
     * @return count
     */
    Long getSystemUserCount(SystemUserQueryRequest request);

    /**
     * 通过 id 删除用户
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemUserById(Long id);

    /**
     * 通过 id 批量删除用户
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteSystemUserByIdList(List<Long> idList);

    /**
     * 通过 idList 删除用户拓展信息
     *
     * @param idList idList
     */
    void deleteSystemUserListRelAsync(List<Long> idList);

    /**
     * 重置密码
     *
     * @param request request
     */
    void resetPassword(UserResetPasswordRequest request);

    /**
     * 获取查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<SystemUserDO> buildQueryWrapper(SystemUserQueryRequest request);

}
