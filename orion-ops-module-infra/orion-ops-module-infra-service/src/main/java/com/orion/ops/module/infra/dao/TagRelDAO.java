package com.orion.ops.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.TagRelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签引用 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Mapper
public interface TagRelDAO extends IMapper<TagRelDO> {

    /**
     * 获取查询条件
     *
     * @param entity entity
     * @return 查询条件
     */
    default LambdaQueryWrapper<TagRelDO> queryCondition(TagRelDO entity) {
        return this.wrapper()
                .eq(TagRelDO::getId, entity.getId())
                .eq(TagRelDO::getTagId, entity.getTagId())
                .eq(TagRelDO::getTagName, entity.getTagName())
                .eq(TagRelDO::getTagType, entity.getTagType())
                .eq(TagRelDO::getRelId, entity.getRelId());
    }

}
