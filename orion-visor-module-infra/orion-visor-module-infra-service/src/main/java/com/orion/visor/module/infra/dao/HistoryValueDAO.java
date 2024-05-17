package com.orion.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.HistoryValueDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 历史归档 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Mapper
public interface HistoryValueDAO extends IMapper<HistoryValueDO> {

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    default int deleteByRelId(String type, Long relId) {
        LambdaQueryWrapper<HistoryValueDO> wrapper = this.lambda()
                .eq(HistoryValueDO::getType, type)
                .eq(HistoryValueDO::getRelId, relId);
        return this.delete(wrapper);
    }

    /**
     * 通过 relId 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    default int deleteByRelIdList(String type, List<Long> relIdList) {
        LambdaQueryWrapper<HistoryValueDO> wrapper = this.lambda()
                .eq(HistoryValueDO::getType, type)
                .in(HistoryValueDO::getRelId, relIdList);
        return this.delete(wrapper);
    }

}
