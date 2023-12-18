package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.DataAliasDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据别名 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Mapper
public interface DataAliasDAO extends IMapper<DataAliasDO> {

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    default int deleteByUserId(Long userId) {
        LambdaQueryWrapper<DataAliasDO> wrapper = this.lambda()
                .eq(DataAliasDO::getUserId, userId);
        return this.delete(wrapper);
    }

}
