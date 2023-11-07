package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.DataGroupRelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据分组关联 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Mapper
public interface DataGroupRelDAO extends IMapper<DataGroupRelDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<DataGroupRelDO> queryCondition(DataGroupRelDO entity) {
        return this.wrapper()
                .eq(DataGroupRelDO::getId, entity.getId())
                .eq(DataGroupRelDO::getGroupId, entity.getGroupId())
                .eq(DataGroupRelDO::getRelId, entity.getRelId())
                .eq(DataGroupRelDO::getType, entity.getType())
                .eq(DataGroupRelDO::getSort, entity.getSort());
    }

}
