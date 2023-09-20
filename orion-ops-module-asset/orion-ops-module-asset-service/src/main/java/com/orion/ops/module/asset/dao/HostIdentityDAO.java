package com.orion.ops.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.HostIdentityDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 主机身份 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostIdentityDAO extends IMapper<HostIdentityDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<HostIdentityDO> queryCondition(HostIdentityDO entity) {
        return this.wrapper()
                .eq(HostIdentityDO::getId, entity.getId())
                .eq(HostIdentityDO::getName, entity.getName())
                .eq(HostIdentityDO::getUsername, entity.getUsername())
                .eq(HostIdentityDO::getPassword, entity.getPassword())
                .eq(HostIdentityDO::getKeyId, entity.getKeyId());
    }

}
