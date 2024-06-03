package com.orion.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.OperatorLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    default int deleteByUserIdList(List<Long> userIdList) {
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.wrapper()
                .in(OperatorLogDO::getUserId, userIdList);
        return this.delete(wrapper);
    }

}
