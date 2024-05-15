package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.CommandSnippetDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 命令片段 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Mapper
public interface CommandSnippetDAO extends IMapper<CommandSnippetDO> {

    /**
     * 设置 groupId 为 null
     *
     * @param groupId groupId
     * @return effect
     */
    default int setGroupIdWithNull(Long groupId) {
        LambdaUpdateWrapper<CommandSnippetDO> wrapper = Wrappers.<CommandSnippetDO>lambdaUpdate()
                .set(CommandSnippetDO::getGroupId, null)
                .eq(CommandSnippetDO::getGroupId, groupId);
        return this.update(null, wrapper);
    }

    /**
     * 通过 groupId 删除
     *
     * @param groupId groupId
     * @return effect
     */
    default int deleteByGroupId(Long groupId) {
        LambdaQueryWrapper<CommandSnippetDO> wrapper = this.lambda()
                .eq(CommandSnippetDO::getGroupId, groupId);
        return this.delete(wrapper);
    }

}
