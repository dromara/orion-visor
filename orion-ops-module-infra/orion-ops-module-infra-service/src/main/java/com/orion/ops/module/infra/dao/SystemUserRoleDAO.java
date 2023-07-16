package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.SystemUserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:31
 */
@Mapper
public interface SystemUserRoleDAO extends IMapper<SystemUserRoleDO> {

    /**
     * 获取全部条件
     *
     * @param entity entity
     * @return 全部条件
     */
    default LambdaQueryWrapper<SystemUserRoleDO> condition(SystemUserRoleDO entity) {
        return this.wrapper()
                .eq(SystemUserRoleDO::getId, entity.getId())
                .eq(SystemUserRoleDO::getUserId, entity.getUserId())
                .eq(SystemUserRoleDO::getRoleId, entity.getRoleId());
    }

    /**
     * 查询用户的全部角色id
     *
     * @param userId userId
     * @return roleId
     */
    default List<Long> selectRoleByUserId(Long userId) {
        LambdaQueryWrapper<SystemUserRoleDO> wrapper = this.wrapper()
                .select(SystemUserRoleDO::getRoleId)
                .eq(SystemUserRoleDO::getUserId, userId);
        return this.selectList(wrapper).stream()
                .map(SystemUserRoleDO::getRoleId)
                .collect(Collectors.toList());
    }

}
