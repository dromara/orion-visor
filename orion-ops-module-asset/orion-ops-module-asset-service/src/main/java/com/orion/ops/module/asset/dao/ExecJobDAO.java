package com.orion.ops.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.ExecJobDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划执行任务 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobDAO extends IMapper<ExecJobDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<ExecJobDO> queryCondition(ExecJobDO entity) {
        return this.wrapper()
                .eq(ExecJobDO::getId, entity.getId())
                .eq(ExecJobDO::getName, entity.getName())
                .eq(ExecJobDO::getExpression, entity.getExpression())
                .eq(ExecJobDO::getTimeout, entity.getTimeout())
                .eq(ExecJobDO::getCommand, entity.getCommand())
                .eq(ExecJobDO::getParameterSchema, entity.getParameterSchema())
                .eq(ExecJobDO::getStatus, entity.getStatus())
                .eq(ExecJobDO::getRecentLogId, entity.getRecentLogId());
    }

}
