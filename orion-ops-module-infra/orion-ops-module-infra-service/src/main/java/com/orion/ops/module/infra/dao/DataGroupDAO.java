package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.DataGroupDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据分组 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupDAO extends IMapper<DataGroupDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<DataGroupDO> queryCondition(DataGroupDO entity) {
        return this.wrapper()
                .eq(DataGroupDO::getId, entity.getId())
                .eq(DataGroupDO::getParentId, entity.getParentId())
                .eq(DataGroupDO::getName, entity.getName())
                .eq(DataGroupDO::getType, entity.getType())
                .eq(DataGroupDO::getSort, entity.getSort());
    }

}
