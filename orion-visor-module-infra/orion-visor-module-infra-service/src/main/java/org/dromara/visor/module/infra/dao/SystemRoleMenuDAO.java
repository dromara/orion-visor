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
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.SystemRoleMenuDO;

import java.util.List;

/**
 * 角色菜单关联 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:31
 */
@Mapper
public interface SystemRoleMenuDAO extends IMapper<SystemRoleMenuDO> {

    /**
     * 通过 roleId 删除
     *
     * @param roleId roleId
     * @return effect
     */
    default int deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<SystemRoleMenuDO> wrapper = this.wrapper()
                .eq(SystemRoleMenuDO::getRoleId, roleId);
        return this.delete(wrapper);
    }

    /**
     * 通过 menuId 删除
     *
     * @param menuIdList menuIdList
     * @return effect
     */
    default int deleteByMenuId(List<Long> menuIdList) {
        LambdaQueryWrapper<SystemRoleMenuDO> wrapper = this.wrapper()
                .in(SystemRoleMenuDO::getMenuId, menuIdList);
        return this.delete(wrapper);
    }

}