package com.orion.visor.module.infra.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.TagRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

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
     * 查询 tag 关联的所有 id
     *
     * @param tagId tagId
     * @return rel
     */
    default List<Long> selectRelIdByTagId(Long tagId) {
        LambdaQueryWrapper<TagRelDO> wrapper = this.lambda()
                .select(TagRelDO::getRelId)
                .eq(TagRelDO::getTagId, tagId);
        return this.selectList(wrapper)
                .stream()
                .map(TagRelDO::getRelId)
                .collect(Collectors.toList());
    }

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagIdList tagIdList
     * @return rel
     */
    default List<Long> selectRelIdByTagId(List<Long> tagIdList) {
        LambdaQueryWrapper<TagRelDO> wrapper = this.lambda()
                .select(TagRelDO::getRelId)
                .in(TagRelDO::getTagId, tagIdList);
        return this.selectList(wrapper)
                .stream()
                .map(TagRelDO::getRelId)
                .collect(Collectors.toList());
    }

}
