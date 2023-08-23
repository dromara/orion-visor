package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:31
 */
@Mapper
public interface SystemMenuDAO extends IMapper<SystemMenuDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<SystemMenuDO> queryCondition(SystemMenuDO entity) {
        return this.wrapper()
                .eq(SystemMenuDO::getId, entity.getId())
                .eq(SystemMenuDO::getParentId, entity.getParentId())
                .eq(SystemMenuDO::getName, entity.getName())
                .eq(SystemMenuDO::getPermission, entity.getPermission())
                .eq(SystemMenuDO::getType, entity.getType())
                .eq(SystemMenuDO::getSort, entity.getSort())
                .eq(SystemMenuDO::getStatus, entity.getStatus())
                .eq(SystemMenuDO::getCache, entity.getCache())
                .eq(SystemMenuDO::getIcon, entity.getIcon())
                .eq(SystemMenuDO::getPath, entity.getPath())
                .eq(SystemMenuDO::getComponent, entity.getComponent());
    }

}
