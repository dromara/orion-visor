package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

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
