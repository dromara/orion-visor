package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.DataExtraDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据拓展信息 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Mapper
public interface DataExtraDAO extends IMapper<DataExtraDO> {

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    default int deleteByUserId(Long userId) {
        LambdaQueryWrapper<DataExtraDO> wrapper = this.lambda()
                .eq(DataExtraDO::getUserId, userId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    default int deleteByRelId(String type, Long relId) {
        LambdaQueryWrapper<DataExtraDO> wrapper = this.lambda()
                .eq(DataExtraDO::getType, type)
                .eq(DataExtraDO::getRelId, relId);
        return this.delete(wrapper);
    }

}
