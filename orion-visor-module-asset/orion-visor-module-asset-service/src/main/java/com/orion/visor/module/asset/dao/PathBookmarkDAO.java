package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.PathBookmarkDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 路径标签 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Mapper
public interface PathBookmarkDAO extends IMapper<PathBookmarkDO> {

    /**
     * 设置 groupId 为 null
     *
     * @param groupId groupId
     * @return effect
     */
    default int setGroupIdWithNull(Long groupId) {
        LambdaUpdateWrapper<PathBookmarkDO> wrapper = Wrappers.<PathBookmarkDO>lambdaUpdate()
                .set(PathBookmarkDO::getGroupId, null)
                .eq(PathBookmarkDO::getGroupId, groupId);
        return this.update(null, wrapper);
    }

    /**
     * 通过 groupId 删除
     *
     * @param groupId groupId
     * @return effect
     */
    default int deleteByGroupId(Long groupId) {
        LambdaQueryWrapper<PathBookmarkDO> wrapper = this.lambda()
                .eq(PathBookmarkDO::getGroupId, groupId);
        return this.delete(wrapper);
    }

}
