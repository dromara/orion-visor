package com.orion.ops.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.HostDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Mapper
public interface HostDAO extends IMapper<HostDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<HostDO> queryCondition(HostDO entity) {
        return this.wrapper()
                .eq(HostDO::getId, entity.getId())
                .eq(HostDO::getName, entity.getName())
                .eq(HostDO::getCode, entity.getCode())
                .eq(HostDO::getAddress, entity.getAddress());
    }

}
