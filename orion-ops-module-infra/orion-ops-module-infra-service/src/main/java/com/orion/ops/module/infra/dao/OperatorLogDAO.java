package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.OperatorLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Mapper
public interface OperatorLogDAO extends IMapper<OperatorLogDO> {

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    default int deleteByUserId(Long userId) {
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.wrapper()
                .eq(OperatorLogDO::getUserId, userId);
        return this.delete(wrapper);
    }

}
