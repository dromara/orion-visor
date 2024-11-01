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
package org.dromara.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;

import java.util.Collection;
import java.util.List;

/**
 * 角色 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:31
 */
@Mapper
public interface SystemRoleDAO extends IMapper<SystemRoleDO> {

    /**
     * 通过编码查询角色
     *
     * @param codeList codeList
     * @return roles
     */
    default List<SystemRoleDO> selectByCodeList(Collection<String> codeList) {
        LambdaQueryWrapper<SystemRoleDO> wrapper = this.wrapper()
                .in(SystemRoleDO::getCode, codeList);
        return this.selectList(wrapper);
    }

    /**
     * 通过 userId 和 roleCode 查询 roleId (检查用户是否包含某个角色)
     *
     * @param userId   userId
     * @param codeList codeList
     * @return roleId
     */
    List<Long> getRoleIdByUserIdAndRoleCode(@Param("userId") Long userId,
                                            @Param("codeList") List<String> codeList);

    /**
     * 通过 roleId 和 permission 查询 permission (检查角色是否包含某个权限)
     *
     * @param roleIdList     roleIdList
     * @param permissionList permissionList
     * @return permission
     */
    List<String> getPermissionByRoleIdAndPermission(@Param("roleIdList") List<Long> roleIdList,
                                                    @Param("permissionList") List<String> permissionList);

    /**
     * 查询用户角色
     *
     * @param userId userId
     * @return roles
     */
    List<SystemRoleDO> selectRoleByUserId(@Param("userId") Long userId);

}
