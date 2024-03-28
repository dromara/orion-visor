package com.orion.ops.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.ExecJobHostDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划执行任务主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobHostDAO extends IMapper<ExecJobHostDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<ExecJobHostDO> queryCondition(ExecJobHostDO entity) {
        return this.wrapper()
                .eq(ExecJobHostDO::getId, entity.getId())
                .eq(ExecJobHostDO::getJobId, entity.getJobId())
                .eq(ExecJobHostDO::getHostId, entity.getHostId());
    }

}
