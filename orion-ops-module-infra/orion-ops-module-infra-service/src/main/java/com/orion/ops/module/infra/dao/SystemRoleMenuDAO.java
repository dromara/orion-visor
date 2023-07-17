package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

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
     * 获取全部条件
     *
     * @param entity entity
     * @return 全部条件
     */
    default LambdaQueryWrapper<SystemRoleMenuDO> condition(SystemRoleMenuDO entity) {
        return this.wrapper()
                .eq(SystemRoleMenuDO::getId, entity.getId())
                .eq(SystemRoleMenuDO::getRoleId, entity.getRoleId())
                .eq(SystemRoleMenuDO::getMenuId, entity.getMenuId());
    }

    /**
     * 通过 roleId 删除
     *
     * @param roleId roleId
     * @return effect
     */
    default Integer deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<SystemRoleMenuDO> wrapper = this.wrapper()
                .eq(SystemRoleMenuDO::getRoleId, roleId);
        return this.delete(wrapper);
    }

}
