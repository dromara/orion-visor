package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.TagDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签枚举 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Mapper
public interface TagDAO extends IMapper<TagDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<TagDO> queryCondition(TagDO entity) {
        return this.wrapper()
                .eq(TagDO::getId, entity.getId())
                .eq(TagDO::getName, entity.getName())
                .eq(TagDO::getType, entity.getType());
    }

}
