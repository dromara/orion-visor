package com.orion.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.DataPermissionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据权限 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Mapper
public interface DataPermissionDAO extends IMapper<DataPermissionDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<DataPermissionDO> queryCondition(DataPermissionDO entity) {
        return this.wrapper()
                .eq(DataPermissionDO::getId, entity.getId())
                .eq(DataPermissionDO::getUserId, entity.getUserId())
                .eq(DataPermissionDO::getRoleId, entity.getRoleId())
                .eq(DataPermissionDO::getRelId, entity.getRelId())
                .eq(DataPermissionDO::getType, entity.getType());
    }

}
