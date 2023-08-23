package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:31
 */
@Mapper
public interface SystemUserDAO extends IMapper<SystemUserDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<SystemUserDO> queryCondition(SystemUserDO entity) {
        return this.wrapper()
                .eq(SystemUserDO::getId, entity.getId())
                .eq(SystemUserDO::getUsername, entity.getUsername())
                .eq(SystemUserDO::getPassword, entity.getPassword())
                .eq(SystemUserDO::getNickname, entity.getNickname())
                .eq(SystemUserDO::getAvatar, entity.getAvatar())
                .eq(SystemUserDO::getMobile, entity.getMobile())
                .eq(SystemUserDO::getEmail, entity.getEmail())
                .eq(SystemUserDO::getStatus, entity.getStatus())
                .eq(SystemUserDO::getLastLoginTime, entity.getLastLoginTime());
    }

}
